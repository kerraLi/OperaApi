package com.ywxt.Service.Ali.Impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.cdn.model.v20141111.*;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.ywxt.Dao.Ali.Impl.AliAccountDaoImpl;
import com.ywxt.Dao.Ali.Impl.AliCdnDaoImpl;
import com.ywxt.Dao.Ali.Impl.AliCdnTaskDaoImpl;
import com.ywxt.Domain.Ali.AliAccount;
import com.ywxt.Domain.Ali.AliCdn;
import com.ywxt.Domain.Ali.AliCdnTask;
import com.ywxt.Enum.AliRegion;
import com.ywxt.Service.Ali.AliCdnService;
import com.ywxt.Service.Impl.ParameterIgnoreServiceImpl;
import com.ywxt.Utils.ArrayUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AliCdnServiceImpl extends AliServiceImpl implements AliCdnService {


    private String accessKeyId;
    private String accessKeySecret;
    private HashMap<String, String> userNameMap = new HashMap<>();

    public AliCdnServiceImpl() {
    }

    public AliCdnServiceImpl(String accessKeyId) throws Exception {
        AliAccount aliAccount = new AliAccountDaoImpl().getAliAccount(accessKeyId);
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = aliAccount.getAccessKeySecret();
    }

    public AliCdnServiceImpl(String keyId, String keySecret) {
        this.accessKeyId = keyId;
        this.accessKeySecret = keySecret;
    }

    // 获取dash数据
    public HashMap<String, Object> getDashData() throws Exception {
        HashMap<String, Object> resultParams = new HashMap<String, Object>();
        // normal invalid
        HashMap<String, Object> params = new HashMap<String, Object>();
        for (Object[] os : new AliCdnDaoImpl().getCountGroup(params)) {
            if (os[0].equals("online")) {
                resultParams.put("ali-cdn-" + os[1] + "-normal", os[2]);
            } else {
                resultParams.put("ali-cdn-" + os[1] + "-invalid", os[2]);
            }
        }
        // deprecated
        params = new HashMap<String, Object>();
        params.put("ifMarked", "true");
        for (Object[] os : this.getCdnTotalByAccount(params)) {
            resultParams.put("ali-cdn-" + os[0] + "-deprecated", os[1]);
        }
        return resultParams;
    }

    // CDN-获取个数按account分组
    public List<Object[]> getCdnTotalByAccount(HashMap<String, Object> params) throws Exception {
        // 是否弃用标记
        String coulmn = new ParameterIgnoreServiceImpl().getMarkKey(AliCdn.class);
        String[] markeValues = new ParameterIgnoreServiceImpl().getMarkedValues(AliCdn.class);
        HashMap<String, Object> filterParams = this.filterParamMarked(params, coulmn, markeValues);
        return new AliCdnDaoImpl().getCdnTotalByAccount(filterParams);
    }

    // CDN-获取个数
    public int getCdnTotal(HashMap<String, Object> params) throws Exception {
        // 是否弃用标记
        String coulmn = new ParameterIgnoreServiceImpl().getMarkKey(AliCdn.class);
        String[] markeValues = new ParameterIgnoreServiceImpl().getMarkedValues(AliCdn.class);
        HashMap<String, Object> filterParams = this.filterParamMarked(params, coulmn, markeValues);
        return new AliCdnDaoImpl().getCdnTotal(filterParams);
    }

    // CDN-获取单个
    public AliCdn getCdn(int id) {
        return new AliCdnDaoImpl().getCdn(id);
    }

    // CDN-域名列表&分页信息
    public JSONObject getCdnDomainList(HashMap<String, Object> params, int pageNumber, int pageSize) throws Exception {
        // 是否弃用标记
        String coulmn = new ParameterIgnoreServiceImpl().getMarkKey(AliCdn.class);
        String[] markeValues = new ParameterIgnoreServiceImpl().getMarkedValues(AliCdn.class);
        HashMap<String, Object> filterParams = this.filterParamMarked(params, coulmn, markeValues);
        List<AliCdn> list = new AliCdnDaoImpl().getCdnList(filterParams, pageNumber, pageSize);
        for (AliCdn ac : list) {
            if (ArrayUtils.hasString(markeValues, ac.getDomainName())) {
                ac.setAlertMarked(true);
            }
            ac.setUserName(this.getUserName(ac.getAccessKeyId()));
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", new AliCdnDaoImpl().getCdnTotal(filterParams));
        jsonObject.put("items", list);
        return jsonObject;
    }

    // CDN-刷新&预热
    // 刷新后task存入本地数据库 & 综合所有账号
    public Map<String, String> refreshCdn(String operateType, String refreshType, String objectPath) throws Exception {
        String regex = "[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+\\.?";
        Pattern p = Pattern.compile(regex);
        String[] objectPaths = objectPath.split("\\n");
        // 默认未同一个账号内，若为不同账号更新失败
        Matcher matcher = p.matcher(objectPaths[0]);
        if (matcher.find()) {
            String domain = matcher.group();
            if (this.accessKeyId == null) {
                AliCdn c = new AliCdnDaoImpl().getCdn(domain);
                if (c == null) {
                    throw new Exception("输入DOMAIN错误。");
                }
                this.accessKeyId = c.getAccessKeyId();
                this.accessKeySecret = this.getAccessKeySecret(this.accessKeyId);
            }
        }
        Map<String, String> result = new HashMap<>();
        if (operateType.equals("refresh")) {
            // 刷新
            String objectType = "File";
            if (refreshType.equals("directory")) {
                objectType = "Directory";
            }
            result = this.refreshCdnObjectCaches(objectPath, objectType);
        } else if (operateType.equals("warm")) {
            // 预热
            result = this.pushObjectCache(objectPath);
        } else {
            throw new Exception("error OperateType");
        }
        // 多个任务id
        String[] taskIds = (result.get("taskIds")).split(",");
        List<DescribeRefreshTasksResponse.CDNTask> cdnTasks = this.getCdnRefreshTask(taskIds.length, 1);
        for (DescribeRefreshTasksResponse.CDNTask cdnTask : cdnTasks) {
            AliCdnTask aliCdnTask = new AliCdnTask(this.accessKeyId, cdnTask);
            new AliCdnTaskDaoImpl().saveCdnTask(aliCdnTask);
        }
        return result;
    }

    // CDN-刷新
    private Map<String, String> refreshCdnObjectCaches(String objectPath, String objectType) throws Exception {
        IClientProfile profile = DefaultProfile.getProfile(AliRegion.QINGDAO.getRegion(), this.accessKeyId, this.accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
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

    // CDN-预热
    private Map<String, String> pushObjectCache(String objectPath) throws Exception {
        IClientProfile profile = DefaultProfile.getProfile(AliRegion.QINGDAO.getRegion(), this.accessKeyId, this.accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
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

    // CDN-刷新预热任务列表
    public List<DescribeRefreshTasksResponse.CDNTask> getCdnRefreshTask(String taskId) throws Exception {
        IClientProfile profile = DefaultProfile.getProfile(AliRegion.QINGDAO.getRegion(), this.accessKeyId, this.accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        DescribeRefreshTasksRequest request = new DescribeRefreshTasksRequest();
        request.setTaskId(taskId);
        DescribeRefreshTasksResponse response = client.getAcsResponse(request);
        return response.getTasks();
    }

    // CDN-刷新预热任务列表(page)
    public List<DescribeRefreshTasksResponse.CDNTask> getCdnRefreshTask(int pageSize, int pageNumber) throws Exception {
        IClientProfile profile = DefaultProfile.getProfile(AliRegion.QINGDAO.getRegion(), this.accessKeyId, this.accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        DescribeRefreshTasksRequest request = new DescribeRefreshTasksRequest();
        request.setPageSize(pageSize);
        request.setPageNumber(pageNumber);
        DescribeRefreshTasksResponse response = client.getAcsResponse(request);
        return response.getTasks();
    }

    // CDN-刷新预热任务列表(page&从数据库中读取)
    public JSONObject getCdnRefreshTaskList(HashMap<String, Object> params, int pageSize, int pageNumber) throws Exception {
        List<AliCdnTask> list = new AliCdnTaskDaoImpl().getList(params, pageNumber, pageSize);
        for (AliCdnTask act : list) {
            // 更新状态&进度
            if (act.getStatus().equals("Refreshing") || act.getStatus().equals("Pending")) {
                if (this.accessKeyId == null || this.accessKeySecret == null) {
                    this.accessKeyId = act.getAccessKeyId();
                    this.accessKeySecret = this.getAccessKeySecret(this.accessKeyId);
                }
                // 接口只支持查3天内数据
                List<DescribeRefreshTasksResponse.CDNTask> l = this.getCdnRefreshTask(act.getTaskId());
                if (l.size() > 0) {
                    DescribeRefreshTasksResponse.CDNTask temp = l.get(0);
                    act.setProcess(temp.getProcess());
                    act.setStatus(temp.getStatus());
                    new AliCdnTaskDaoImpl().saveCdnTask(act);
                }
            }
            act.setUserName(this.getUserName(act.getAccessKeyId()));
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", new AliCdnTaskDaoImpl().getTotal(params));
        jsonObject.put("items", list);
        return jsonObject;
    }

    // CDN-刷新预热任务(更新process与status)
    public AliCdnTask updateCdnRefreshTask(int id) throws Exception {
        AliCdnTask act = new AliCdnTaskDaoImpl().getCdnTask(id);
        if (this.accessKeyId == null || this.accessKeySecret == null) {
            this.accessKeyId = act.getAccessKeyId();
            this.accessKeySecret = this.getAccessKeySecret(this.accessKeyId);
        }
        // 接口只支持查3天内数据
        List<DescribeRefreshTasksResponse.CDNTask> l = this.getCdnRefreshTask(act.getTaskId());
        if (l.size() > 0) {
            DescribeRefreshTasksResponse.CDNTask temp = l.get(0);
            act.setProcess(temp.getProcess());
            act.setStatus(temp.getStatus());
            new AliCdnTaskDaoImpl().saveCdnTask(act);
        } else {
            throw new Exception("接口只只差查询3天内数据，下次请及时查看刷新哦~若需查看具体完成情况，可登陆阿里云查看。");
        }
        return act;
    }

}
