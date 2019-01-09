package com.ywxt.Service.Ali.Impl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.bssopenapi.model.v20171214.QueryAccountBalanceRequest;
import com.aliyuncs.bssopenapi.model.v20171214.QueryAccountBalanceResponse;
import com.aliyuncs.cdn.model.v20141111.*;
import com.aliyuncs.ecs.model.v20140526.*;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.alibaba.fastjson.JSONObject;
import com.ywxt.Dao.Ali.AliAccountDao;
import com.ywxt.Dao.Ali.AliCdnDao;
import com.ywxt.Dao.Ali.AliEcsDao;
import com.ywxt.Dao.Ali.Impl.AliAccountDaoImpl;
import com.ywxt.Dao.Ali.Impl.AliCdnDaoImpl;
import com.ywxt.Dao.Ali.Impl.AliEcsDaoImpl;
import com.ywxt.Domain.Ali.AliAccount;
import com.ywxt.Domain.Ali.AliCdn;
import com.ywxt.Domain.Ali.AliEcs;
import com.ywxt.Enum.AliRegion;
import com.ywxt.Service.Ali.AliService;
import com.ywxt.Utils.Parameter;

import java.util.*;

public class AliServiceImpl implements AliService {

    private String accessKeyId;
    private String accessKeySecret;
    private AliAccountDao aliAccountDao = new AliAccountDaoImpl();
    private AliEcsDao aliEcsDao = new AliEcsDaoImpl();
    private AliCdnDao aliCdnDao = new AliCdnDaoImpl();

    public AliServiceImpl() {
    }

    public AliServiceImpl(String accessKeyId) {
        AliAccount aliAccount = this.aliAccountDao.getAliAccount(accessKeyId);
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = aliAccount.getAccessKeySecret();
    }

    public AliServiceImpl(String keyId, String keySecret) {
        this.accessKeyId = keyId;
        this.accessKeySecret = keySecret;
    }

    // 账户
    public QueryAccountBalanceResponse.Data getAccountBalance() throws Exception {
        IClientProfile profile = DefaultProfile.getProfile("", this.accessKeyId, this.accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        QueryAccountBalanceRequest request = new QueryAccountBalanceRequest();
        request.setEndpoint("business.aliyuncs.com");
        QueryAccountBalanceResponse response = client.getAcsResponse(request);
        return response.getData();
    }

    // 查询可用区
    // todo
    public void getRegion() throws Exception {
        IClientProfile profile = DefaultProfile.getProfile("", this.accessKeyId, this.accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
    }

    // 更新ali源数据(按账户更新)
    public void freshSourceData() throws Exception {
        // 更新ecs数据 && 更新cdn域名数据
        this.freshEcsData();
        this.freshCdnData();
    }

    // 更新ecs数据
    public void freshEcsData() throws Exception {
        new AliEcsDaoImpl().deleteAliEcsByAccessId(this.accessKeyId);
        List<AliEcs> aeList = new ArrayList<>();
        for (AliRegion e : AliRegion.values()) {
            IClientProfile profile = DefaultProfile.getProfile(e.getRegion(), this.accessKeyId, this.accessKeySecret);
            IAcsClient client = new DefaultAcsClient(profile);
            int pageSize = 20;
            // ecs
            int pageNumber = 1;
            while (true) {
                DescribeInstancesRequest describeInstancesRequest = new DescribeInstancesRequest();
                describeInstancesRequest.setPageSize(pageSize);
                describeInstancesRequest.setPageNumber(pageNumber);
                DescribeInstancesResponse describeInstancesResponse = client.getAcsResponse(describeInstancesRequest);
                // aeList
                for (DescribeInstancesResponse.Instance i : describeInstancesResponse.getInstances()) {
                    AliEcs ecs = new AliEcs(this.accessKeyId, i);
                    aeList.add(ecs);
                }
                // 最后一页 跳出
                if (describeInstancesResponse.getInstances().size() < pageSize) {
                    break;
                }
                pageNumber++;
            }
        }
        this.aliEcsDao.saveAliEcses(aeList);
    }

    // 更新cdn数据
    public void freshCdnData() throws Exception {
        new AliCdnDaoImpl().deleteAliCdnByAccessId(this.accessKeyId);
        List<AliCdn> acList = new ArrayList<>();
        for (AliRegion e : AliRegion.values()) {
            IClientProfile profile = DefaultProfile.getProfile(e.getRegion(), this.accessKeyId, this.accessKeySecret);
            IAcsClient client = new DefaultAcsClient(profile);
            int pageSize = 20;
            int pageNumber = 1;
            while (true) {
                DescribeUserDomainsRequest describeUserDomainsRequest = new DescribeUserDomainsRequest();
                describeUserDomainsRequest.setPageSize(pageSize);
                describeUserDomainsRequest.setPageNumber(pageNumber);
                DescribeUserDomainsResponse describeUserDomainsResponse = client.getAcsResponse(describeUserDomainsRequest);
                // acList
                for (DescribeUserDomainsResponse.PageData domain : describeUserDomainsResponse.getDomains()) {
                    AliCdn cdnDomain = new AliCdn(this.accessKeyId, domain);
                    acList.add(cdnDomain);
                }
                // 最后一页 跳出
                if (describeUserDomainsResponse.getDomains().size() < pageSize) {
                    break;
                }
                pageNumber++;
            }
        }
        this.aliCdnDao.saveAliCdns(acList);
    }

    // ecs-查询所有
    public List<AliEcs> getEcsList(HashMap<String, Object> params) throws Exception {
        List<AliEcs> list = this.aliEcsDao.getAliEcsesList(params);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, Integer.parseInt(Parameter.alertThresholds.get("ALI_ECS_EXPIRED_DAY")));
        Date thresholdDate = calendar.getTime();
        for (AliEcs ae : list) {
            if (ae.getStatus().equals("Running")) {
                ae.setAlertExpired(ae.getExpiredTime().before(thresholdDate));
            }
        }
        return list;
    }

    // ecs-查询所有实例的详细信息&分页
    public JSONObject getEcsListPage(HashMap<String, Object> params, int pageNumber, int pageSize) throws Exception {
        List<AliEcs> list = this.aliEcsDao.getAliEcsesList(params, pageNumber, pageSize);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, Integer.parseInt(Parameter.alertThresholds.get("ALI_ECS_EXPIRED_DAY")));
        Date thresholdDate = calendar.getTime();
        for (AliEcs ae : list) {
            if (ae.getStatus().equals("Running")) {
                ae.setAlertExpired(ae.getExpiredTime().before(thresholdDate));
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", this.aliEcsDao.getAliEcsesTotal(params));
        jsonObject.put("items", list);
        return jsonObject;
    }

    // ecs-启动
    public void startEcs(String regionId, String instanceId) throws Exception {
        IClientProfile profile = DefaultProfile.getProfile(regionId, this.accessKeyId, this.accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        StartInstanceRequest request = new StartInstanceRequest();
        request.setInstanceId(instanceId);
        client.getAcsResponse(request);
    }

    // ecs-停止
    public void stopEcs(String regionId, String instanceId, boolean forceStop) throws Exception {
        IClientProfile profile = DefaultProfile.getProfile(regionId, this.accessKeyId, this.accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        StopInstanceRequest request = new StopInstanceRequest();
        request.setInstanceId(instanceId);
        request.setForceStop(forceStop);
        client.getAcsResponse(request);
    }

    // ecs-重启
    public void restartEcs(String regionId, String instanceId, boolean forceStop) throws Exception {
        IClientProfile profile = DefaultProfile.getProfile(regionId, this.accessKeyId, this.accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        RebootInstanceRequest request = new RebootInstanceRequest();
        request.setInstanceId(instanceId);
        request.setForceStop(forceStop);
        client.getAcsResponse(request);
    }

    // ecs-释放
    public void deleteEcs(String regionId, String instanceId, boolean force) throws Exception {
        IClientProfile profile = DefaultProfile.getProfile(regionId, this.accessKeyId, this.accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        DeleteInstanceRequest request = new DeleteInstanceRequest();
        request.setInstanceId(instanceId);
        request.setForce(force);
        client.getAcsResponse(request);
    }

    // CDN-域名列表&分页信息
    public JSONObject getCdnDomainList(HashMap<String, Object> params, int pageNumber, int pageSize) throws Exception {
        List<AliCdn> list = this.aliCdnDao.getCdnList(params, pageNumber, pageSize);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", this.aliCdnDao.getCdnTotal(params));
        jsonObject.put("items", list);
        return jsonObject;
    }

    // CDN-刷新
    public Map<String, String> refreshCdnObjectCaches(String objectPath, String objectType) throws Exception {
        IClientProfile profile = DefaultProfile.getProfile(AliRegion.QINGDAO.getRegion(), this.accessKeyId, this.accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        RefreshObjectCachesRequest request = new RefreshObjectCachesRequest();
        // 设置刷新域名多个URL使用换行符分隔"\n"或者"\r\n"
        request.setObjectPath(objectPath);
        // 可选，刷新类型值为"File"或"Directory"；默认为"File"
        request.setObjectType(objectType);
        RefreshObjectCachesResponse response = client.getAcsResponse(request);
        Map<String, String> map = new HashMap<>();
        map.put("refreshTaskId", response.getRefreshTaskId());
        map.put("requestId", response.getRequestId());
        return map;
    }

    // CDN-刷新预热任务
    public List<DescribeRefreshTasksResponse.CDNTask> getCdnRefreshTask(String taskId) throws Exception {
        IClientProfile profile = DefaultProfile.getProfile(AliRegion.QINGDAO.getRegion(), this.accessKeyId, this.accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        DescribeRefreshTasksRequest request = new DescribeRefreshTasksRequest();
        request.setTaskId(taskId);
        DescribeRefreshTasksResponse response = client.getAcsResponse(request);
        return response.getTasks();
    }

}
