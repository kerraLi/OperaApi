package com.ywxt.Service.Ali.Impl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.scdn.model.v20171115.*;
import com.ywxt.Dao.Ali.AliAccountDao;
import com.ywxt.Dao.Ali.AliScdnDao;
import com.ywxt.Dao.Ali.AliScdnTaskDao;
import com.ywxt.Domain.Ali.AliAccount;
import com.ywxt.Domain.Ali.AliScdn;
import com.ywxt.Domain.Ali.AliScdnTask;
import com.ywxt.Domain.in.AliScdnIn;
import com.ywxt.Exception.MTMException;
import com.ywxt.Utils.ExceptionUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AliScdnService {

    @Autowired
    private AliScdnDao aliScdnDao;
    @Autowired
    private AliScdnTaskDao aliScdnTaskDao;
    @Autowired
    private AliAccountDao aliAccountDao;

    /**
     * 分页查询
     * @param aliScdn
     * @param page
     * @param limit
     * @return
     */
    public Page<AliScdn> getList(AliScdn aliScdn, Integer page, Integer limit){

        Specification<AliScdn> specification = new Specification<AliScdn>() {
            @Override
            public Predicate toPredicate(Root<AliScdn> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if(StringUtils.isNoneBlank(aliScdn.getUserName())){
                    Predicate predicate = criteriaBuilder.like(root.get("userName").as(String.class),"%"+aliScdn.getUserName()+"%");
                    predicates.add(predicate);
                }
                if(StringUtils.isNoneBlank(aliScdn.getDomainName())){
                    Predicate predicate = criteriaBuilder.like(root.get("domainName").as(String.class),"%"+aliScdn.getDomainName()+"%");
                    predicates.add(predicate);
                }
                if(StringUtils.isNoneBlank(aliScdn.getDomainStatus())){
                    Predicate predicate = criteriaBuilder.equal(root.get("domainStatus").as(String.class),aliScdn.getDomainStatus());
                    predicates.add(predicate);
                }
                if(StringUtils.isNoneBlank(aliScdn.getSslProtocol())){
                    Predicate predicate = criteriaBuilder.equal(root.get("sslProtocol").as(String.class),aliScdn.getSslProtocol());
                    predicates.add(predicate);
                }
                if(StringUtils.isNoneBlank(aliScdn.getAbandon())){
                    Predicate predicate = criteriaBuilder.equal(root.get("abandon").as(String.class),aliScdn.getAbandon());
                    predicates.add(predicate);
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        Pageable pageable = PageRequest.of(page-1,limit);
        return aliScdnDao.findAll(specification,pageable);
    }

    @Transactional
    public void updateAbandon(AliScdnIn aliScdnIn) {
        List<AliScdn> aliScdns = aliScdnDao.findAllById(aliScdnIn.getIds());
        for(AliScdn aliScdn:aliScdns){
            aliScdn.setAbandon(aliScdnIn.getAbandon());
        }
        aliScdnDao.saveAll(aliScdns);
    }

    @Transactional
    public void addRefresh(AliScdnIn aliScdnIn) throws Exception {
        String regex = "[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+\\.?";
        Pattern p = Pattern.compile(regex);
        String[] objectPaths = aliScdnIn.getContent().split("\\n");
        // 默认未同一个账号内，若为不同账号更新失败
        Matcher matcher = p.matcher(objectPaths[0]);
        String domain = null;
        if (matcher.find()) {
             domain = matcher.group();
        }
        ExceptionUtil.isTrue(domain==null,"url错误");
        AliScdn scdn = aliScdnDao.findByDomainName(domain);
        ExceptionUtil.isTrue(scdn==null,"url错误");
        AliAccount aliAccount = aliAccountDao.findAliAccountByAccessKeyId(scdn.getAccessKeyId());

        //获取连接
        IAcsClient client = getAliClient(aliAccount);
        //设置参数
        String taskIds1 = "";
        if("refresh".equals(aliScdnIn.getOperateType())){
            String objectType = "File";
            if ("directory".equals(aliScdnIn.getRefreshType())) {
                objectType = "Directory";
            }
                taskIds1 = refreshScdn(aliScdnIn.getContent(),objectType,client);
        }else if("warm".equals(aliScdnIn.getOperateType())){
                taskIds1 = preloadScdn(aliScdnIn.getContent(),client);
        }else {
            new MTMException("操作类型不正确");
        }
        String[] taskIds = taskIds1.split(",");
        //查询状态
        List<AliScdnTask> tasks = describeScdn(taskIds,client,aliAccount);
        //保存到数据库
        aliScdnTaskDao.saveAll(tasks);
    }

    //获取阿里连接
    private IAcsClient getAliClient(AliAccount aliAccount) {
        IClientProfile profile = DefaultProfile.getProfile("",
                aliAccount.getAccessKeyId(), aliAccount.getAccessKeySecret());
        return new DefaultAcsClient(profile);
    }

    //查询状态
    private List<AliScdnTask> describeScdn(String[] taskIds, IAcsClient client,AliAccount account) throws ClientException, ParseException {
        DescribeScdnRefreshTasksRequest request = new DescribeScdnRefreshTasksRequest();
        request.setEndpoint("scdn.aliyuncs.com");
        List<AliScdnTask> list = new ArrayList<>();
        for(String taskId : taskIds){
            request.setTaskId(taskId);
            DescribeScdnRefreshTasksResponse response = client.getAcsResponse(request);
            for(DescribeScdnRefreshTasksResponse.Task task : response.getTasks()){
                AliScdnTask aliScdnTask = new AliScdnTask(account,task);
                list.add(aliScdnTask);
            }
        }
        return list;
    }

    //预热
    private String preloadScdn(String content, IAcsClient client) throws ClientException {
        PreloadScdnObjectCachesRequest request = new PreloadScdnObjectCachesRequest();
        request.setEndpoint("scdn.aliyuncs.com");
        request.setObjectPath(content);
        PreloadScdnObjectCachesResponse response = client.getAcsResponse(request);
        return response.getPreloadTaskId();
    }

    //刷新
    private String refreshScdn(String content, String objectType, IAcsClient client) throws ClientException {
        RefreshScdnObjectCachesRequest request = new RefreshScdnObjectCachesRequest();
        request.setEndpoint("scdn.aliyuncs.com");
        request.setObjectPath(content);
        request.setObjectType(objectType);
        RefreshScdnObjectCachesResponse  response = client.getAcsResponse(request);
        return response.getRefreshTaskId();
    }


    public Page<AliScdnTask> refreshList(AliScdnIn aliScdnIn) {
        if("all".equals(aliScdnIn.getObjectType())){
            aliScdnIn.setObjectType(null);
        }
        Specification<AliScdnTask> specification = new Specification<AliScdnTask>() {
            @Override
            public Predicate toPredicate(Root<AliScdnTask> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if(StringUtils.isNoneBlank(aliScdnIn.getObjectType())){
                    Predicate predicate = criteriaBuilder.equal(root.get("objectType").as(String.class),aliScdnIn.getObjectType());
                    predicates.add(predicate);
                }
                if(StringUtils.isNoneBlank(aliScdnIn.getUrl())){
                    Predicate predicate = criteriaBuilder.like(root.get("objectPath").as(String.class),"%"+aliScdnIn.getUrl()+"%");
                    predicates.add(predicate);
                }
                if(aliScdnIn.getStartTime()!=null&&aliScdnIn.getEndTime()!=null){
                    Predicate less = criteriaBuilder.lessThanOrEqualTo(root.get("creationTime").as(Date.class),aliScdnIn.getEndTime());
                    predicates.add(less);
                    Predicate greater = criteriaBuilder.greaterThanOrEqualTo(root.get("creationTime").as(Date.class),aliScdnIn.getStartTime());
                    predicates.add(greater);
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        Pageable pageable = PageRequest.of(aliScdnIn.getPage()-1,aliScdnIn.getLimit());
        return aliScdnTaskDao.findAll(specification,pageable);
    }

    //更新刷新（预热）状态
    @Transactional
    public void updateScdn(Long id) throws Exception {
        AliScdnTask aliScdnTask = aliScdnTaskDao.getOne(id);
        ExceptionUtil.isTrue(aliScdnTask==null,"id不正确");
        AliAccount aliAccount = aliAccountDao.findAliAccountByAccessKeyId(aliScdnTask.getAccessKeyId());
        //获取连接
        IAcsClient client = getAliClient(aliAccount);

        String[] taskIds = {aliScdnTask.getTaskId()};
        List<AliScdnTask> tasks = describeScdn(taskIds,client,aliAccount);
        ExceptionUtil.isTrue(CollectionUtils.isEmpty(tasks),"接口只能查询3天内数据，下次请及时查看刷新哦~若需查看具体完成情况，可登陆阿里云查看。");
        AliScdnTask task = tasks.get(0);
        aliScdnTask.setProcess(task.getProcess());
        aliScdnTask.setStatus(task.getStatus());
        aliScdnTaskDao.saveAndFlush(aliScdnTask);
    }
}
