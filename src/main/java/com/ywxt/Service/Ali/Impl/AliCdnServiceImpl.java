package com.ywxt.Service.Ali.Impl;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.cdn.model.v20141111.*;
import com.ywxt.Dao.Ali.AliCdnDao;
import com.ywxt.Dao.Ali.AliCdnTaskDao;
import com.ywxt.Domain.Ali.AliCdn;
import com.ywxt.Domain.Ali.AliCdnTask;
import com.ywxt.Enum.AliRegion;
import com.ywxt.Service.Ali.AliCdnService;
import com.ywxt.Service.Ali.AliService;
import com.ywxt.Service.System.IgnoreService;
import com.ywxt.Service.System.ParameterService;
import com.ywxt.Utils.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.Transient;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AliCdnServiceImpl implements AliCdnService {

    private String paramIgnoreDomain = "AliCdn";
    private String paramIgnoreColumn = "domainName";
    private String paramExpiresColumn = "";
    private String paramExpiresKey = "";
    private String paramStatusColumn = "domainStatus";
    private String paramStatusNormal = "online";
    private String[] ParamStatusExcept = {"online", "offline"};

    @Autowired
    private AliCdnDao aliCdnDao;
    @Autowired
    private AliCdnTaskDao aliCdnTaskDao;
    @Autowired
    private AliService aliService;
    @Autowired
    private IgnoreService ignoreService;
    @Autowired
    private ParameterService parameterService;

    // CDN-域名列表&分页信息
    public Page<AliCdn> getList(Map<String, String> params) throws Exception {
        int pageNumber = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 1;
        int pageSize = params.containsKey("limit") ? Integer.parseInt(params.get("limit")) : 10;
        // 排除忽略数据
        String[] markValues = ignoreService.getMarkedValues(paramIgnoreDomain);
        // 处理查询条件
        Specification<AliCdn> specification = new Specification<AliCdn>() {
            @Override
            public Predicate toPredicate(Root<AliCdn> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                // filter批量过滤
                if (params.containsKey("key")) {
                    String filter = "%" + params.get("key") + "%";
                    Field[] fields = AliCdn.class.getDeclaredFields();
                    // 多个or条件
                    List<Predicate> psOr = new ArrayList<>();
                    for (Field f : fields) {
                        if (f.getType() == String.class && !f.isAnnotationPresent(Transient.class)) {
                            psOr.add(cb.like(root.get(f.getName()).as(String.class), filter));
                        }
                    }
                    predicates.add(cb.or(psOr.toArray(new Predicate[psOr.size()])));
                }
                // 忽略数据
                if (params.containsKey("ifMarked") && params.get("ifMarked").equals("true")) {
                    // in
                    List<Predicate> psOr = new ArrayList<>();
                    CriteriaBuilder.In<String> in = cb.in(root.get(paramIgnoreColumn));
                    if (markValues.length == 0) {
                        in.value((String) null);
                    } else {
                        for (String s : markValues) {
                            in.value(s);
                        }
                    }
                    predicates.add(in);
                } else {
                    for (String s : markValues) {
                        predicates.add(cb.notEqual(root.get(paramIgnoreColumn).as(String.class), s));
                    }
                }
                // status
                if (params.containsKey("status")) {
                    if (params.get("status").equals("others")) {
                        for (String s : ParamStatusExcept) {
                            predicates.add(cb.notEqual(root.get(paramStatusColumn).as(String.class), s));
                        }
                    } else {
                        predicates.add(cb.equal(root.get(paramStatusColumn).as(String.class), params.get("status")));
                    }
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<AliCdn> page = aliCdnDao.findAll(specification, pageable);
        // 查询后处理
        for (AliCdn ae : page.getContent()) {
            // 弃用
            if (ArrayUtils.hasString(markValues, ae.getDomainName())) {
                ae.setAlertMarked(true);
            }
            ae.setUserName(aliService.getUserName(ae.getAccessKeyId()));
        }
        return page;
    }

    // CDN-TASK-刷新预热任务列表(page&从数据库中读取)
    public Page<AliCdnTask> getTaskList(Map<String, String> params) throws Exception {
        int pageNumber = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 1;
        int pageSize = params.containsKey("limit") ? Integer.parseInt(params.get("limit")) : 10;
        Specification<AliCdnTask> specification = new Specification<AliCdnTask>() {
            @Override
            public Predicate toPredicate(Root<AliCdnTask> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (params.containsKey("operDate")) {
                    String[] operateDate = params.get("operDate").split(",");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        predicates.add(cb.greaterThan(root.get("creationTime"), sdf.parse(operateDate[0] + " 00:00:00")));
                        predicates.add(cb.lessThan(root.get("creationTime"), sdf.parse(operateDate[1] + " 23:59:59")));
                    } catch (ParseException e) {
                    }
                }
                if (params.containsKey("url")) {
                    predicates.add(cb.like(root.get("objectPath"), "%" + params.get("url") + "%"));
                }
                if (params.containsKey("operType")) {
                    switch (params.get("operType")) {
                        case "url-refresh":
                            predicates.add(cb.equal(root.get("objectType"), "file"));
                            break;
                        case "content-refresh":
                            predicates.add(cb.equal(root.get("objectType"), "path"));
                            break;
                        case "url-warm":
                            predicates.add(cb.equal(root.get("objectType"), "preload"));
                            break;
                        default:
                            break;
                    }
                }
                cb.desc(root.get("creationTime"));
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<AliCdnTask> page = aliCdnTaskDao.findAll(specification, pageable);
        // 查询后处理
        for (AliCdnTask act : page.getContent()) {
            // 更新状态&进度
            IAcsClient client = null;
            if (act.getStatus().equals("Refreshing") || act.getStatus().equals("Pending")) {
                if (client == null) {
                    String keyId = act.getAccessKeyId();
                    String keySecret = aliService.getAccessKeySecret(keyId);
                    client = aliService.getAliClient(AliRegion.QINGDAO.getRegion(), keyId, keySecret);
                }
                // 接口只支持查3天内数据
                List<DescribeRefreshTasksResponse.CDNTask> l = this.getCdnRefreshTask(client, act.getTaskId());
                if (l.size() > 0) {
                    DescribeRefreshTasksResponse.CDNTask temp = l.get(0);
                    act.setProcess(temp.getProcess());
                    act.setStatus(temp.getStatus());
                    aliCdnTaskDao.saveAndFlush(act);
                }
            }
            act.setUserName(aliService.getUserName(act.getAccessKeyId()));
        }
        return page;
    }

    // CDN-TASK-刷新预热任务(更新process与status)
    public AliCdnTask updateTask(int id) throws Exception {
        AliCdnTask act = aliCdnTaskDao.findAliCdnTaskById(id);
        String keyId = act.getAccessKeyId();
        String keySecret = aliService.getAccessKeySecret(keyId);
        IAcsClient client = aliService.getAliClient(AliRegion.QINGDAO.getRegion(), keyId, keySecret);
        // 接口只支持查3天内数据
        List<DescribeRefreshTasksResponse.CDNTask> l = this.getCdnRefreshTask(client, act.getTaskId());
        if (l.size() > 0) {
            DescribeRefreshTasksResponse.CDNTask temp = l.get(0);
            act.setProcess(temp.getProcess());
            act.setStatus(temp.getStatus());
            aliCdnTaskDao.saveAndFlush(act);
        } else {
            throw new Exception("接口只只差查询3天内数据，下次请及时查看刷新哦~若需查看具体完成情况，可登陆阿里云查看。");
        }
        return act;
    }

    // CDN-TASK-刷新&预热操作
    // 刷新后task存入本地数据库 & 综合所有账号
    public Map<String, String> refreshCdn(String operateType, String refreshType, String objectPath) throws Exception {
        String regex = "[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+\\.?";
        Pattern p = Pattern.compile(regex);
        String[] objectPaths = objectPath.split("\\n");
        // 默认未同一个账号内，若为不同账号更新失败
        Matcher matcher = p.matcher(objectPaths[0]);
        String domain = null;
        if (matcher.find()) {
            domain = matcher.group();
        } else {
            throw new Exception("输入DOMAIN错误。");
        }
        AliCdn c = aliCdnDao.getByDomainName(domain);
        String keyId = c.getAccessKeyId();
        String keySecret = aliService.getAccessKeySecret(keyId);
        IAcsClient client = aliService.getAliClient(AliRegion.QINGDAO.getRegion(), keyId, keySecret);
        Map<String, String> result = new HashMap<>();
        if (operateType.equals("refresh")) {
            // 刷新
            String objectType = "File";
            if (refreshType.equals("directory")) {
                objectType = "Directory";
            }
            result = this.refreshCdnObjectCaches(client, objectPath, objectType);
        } else if (operateType.equals("warm")) {
            // 预热
            result = this.pushObjectCache(client, objectPath);
        } else {
            throw new Exception("error OperateType");
        }
        // 多个任务id
        String[] taskIds = (result.get("taskIds")).split(",");
        List<DescribeRefreshTasksResponse.CDNTask> cdnTasks = this.getCdnRefreshTask(client, taskIds.length, 1);
        for (DescribeRefreshTasksResponse.CDNTask cdnTask : cdnTasks) {
            aliCdnTaskDao.save(new AliCdnTask(keyId, cdnTask));
        }
        aliCdnTaskDao.flush();
        return result;
    }

    // CDN-TASK-刷新
    private Map<String, String> refreshCdnObjectCaches(IAcsClient client, String objectPath, String objectType) throws Exception {
        RefreshObjectCachesRequest request = new RefreshObjectCachesRequest();
        // 设置刷新域名多个URL使用换行符分隔"\n"或者"\r\n"
        request.setObjectPath(objectPath);
        // 可选，刷新类型值为"File"或"Directory"；默认为"File"
        request.setObjectType(objectType);
        RefreshObjectCachesResponse response = client.getAcsResponse(request);
        Map<String, String> map = new HashMap<>();
        // 多个任务：逗号分隔数组
        map.put("taskIds", response.getRefreshTaskId());
        map.put("requestId", response.getRequestId());
        return map;
    }

    // CDN-TASK-预热
    private Map<String, String> pushObjectCache(IAcsClient client, String objectPath) throws Exception {
        PushObjectCacheRequest request = new PushObjectCacheRequest();
        // 设置刷新域名多个URL使用换行符分隔"\n"或者"\r\n"
        request.setObjectPath(objectPath);
        PushObjectCacheResponse response = client.getAcsResponse(request);
        Map<String, String> map = new HashMap<>();
        // 多个任务：逗号分隔数组
        map.put("taskIds", response.getPushTaskId());
        map.put("requestId", response.getRequestId());
        return map;
    }

    // CDN-TASK-更新-刷新预热任务列表
    private List<DescribeRefreshTasksResponse.CDNTask> getCdnRefreshTask(IAcsClient client, String taskId) throws Exception {
        DescribeRefreshTasksRequest request = new DescribeRefreshTasksRequest();
        request.setTaskId(taskId);
        DescribeRefreshTasksResponse response = client.getAcsResponse(request);
        return response.getTasks();
    }

    // CDN-TASK-更新-刷新预热任务列表(page)
    private List<DescribeRefreshTasksResponse.CDNTask> getCdnRefreshTask(IAcsClient client, int pageSize, int pageNumber) throws Exception {
        DescribeRefreshTasksRequest request = new DescribeRefreshTasksRequest();
        request.setPageSize(pageSize);
        request.setPageNumber(pageNumber);
        DescribeRefreshTasksResponse response = client.getAcsResponse(request);
        return response.getTasks();
    }

}
