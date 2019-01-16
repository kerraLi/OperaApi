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
import com.ywxt.Dao.Ali.Impl.AliAccountDaoImpl;
import com.ywxt.Dao.Ali.Impl.AliCdnDaoImpl;
import com.ywxt.Dao.Ali.Impl.AliEcsDaoImpl;
import com.ywxt.Domain.Ali.AliAccount;
import com.ywxt.Domain.Ali.AliCdn;
import com.ywxt.Domain.Ali.AliEcs;
import com.ywxt.Enum.AliRegion;
import com.ywxt.Service.Ali.AliService;
import com.ywxt.Service.Impl.ParameterIgnoreServiceImpl;
import com.ywxt.Utils.ArrayUtils;
import com.ywxt.Utils.Parameter;

import java.util.*;

public class AliServiceImpl implements AliService {

    private String accessKeyId;
    private String accessKeySecret;
    private HashMap<String, String> userNameMap = new HashMap<>();

    public AliServiceImpl() {
    }

    public AliServiceImpl(String accessKeyId) throws Exception {
        AliAccount aliAccount = new AliAccountDaoImpl().getAliAccount(accessKeyId);
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
        new AliEcsDaoImpl().saveAliEcses(aeList);
    }

    // 更新cdn数据
    public void freshCdnData() throws Exception {
        new AliCdnDaoImpl().deleteAliCdnByAccessId(this.accessKeyId);
        List<AliCdn> acList = new ArrayList<>();
        // cdn不分地区：只查一个地区
        IClientProfile profile = DefaultProfile.getProfile(AliRegion.QINGDAO.getRegion(), this.accessKeyId, this.accessKeySecret);
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
        new AliCdnDaoImpl().saveAliCdns(acList);
    }

    // ecs-获取单个
    public AliEcs getEcs(int id) {
        return new AliEcsDaoImpl().getEcs(id);
    }

    // ecs-查询所有
    public List<AliEcs> getEcsList(HashMap<String, Object> params) throws Exception {
        // 是否弃用标记
        String coulmn = new ParameterIgnoreServiceImpl().getMarkKey(AliEcs.class);
        String[] markeValues = new ParameterIgnoreServiceImpl().getMarkedValues(AliEcs.class);
        HashMap<String, Object> filterParams = this.filterParamMarked(params, coulmn, markeValues);
        List<AliEcs> list = new AliEcsDaoImpl().getAliEcsesList(filterParams);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, Integer.parseInt(Parameter.alertThresholds.get("ALI_ECS_EXPIRED_DAY")));
        Date thresholdDate = calendar.getTime();
        for (AliEcs ae : list) {
            if (ae.getStatus().equals("Running")) {
                ae.setAlertExpired(ae.getExpiredTime().before(thresholdDate));
            }
            if (ArrayUtils.hasString(markeValues, ae.getInstanceId())) {
                ae.setAlertMarked(true);
            }
            ae.setUserName(this.getUserName(ae.getAccessKeyId()));
        }
        return list;
    }

    // ecs-查询所有实例的详细信息&分页
    public JSONObject getEcsList(HashMap<String, Object> params, int pageNumber, int pageSize) throws Exception {
        // 是否弃用标记
        String coulmn = new ParameterIgnoreServiceImpl().getMarkKey(AliEcs.class);
        String[] markeValues = new ParameterIgnoreServiceImpl().getMarkedValues(AliEcs.class);
        HashMap<String, Object> filterParams = this.filterParamMarked(params, coulmn, markeValues);
        List<AliEcs> list = new AliEcsDaoImpl().getAliEcsesList(filterParams, pageNumber, pageSize);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, Integer.parseInt(Parameter.alertThresholds.get("ALI_ECS_EXPIRED_DAY")));
        Date thresholdDate = calendar.getTime();
        for (AliEcs ae : list) {
            if (ae.getStatus().equals("Running")) {
                ae.setAlertExpired(ae.getExpiredTime().before(thresholdDate));
            }
            if (ArrayUtils.hasString(markeValues, ae.getInstanceId())) {
                ae.setAlertMarked(true);
            }
            ae.setUserName(this.getUserName(ae.getAccessKeyId()));
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", new AliEcsDaoImpl().getAliEcsesTotal(filterParams));
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

    // 过滤弃用param
    private HashMap<String, Object> filterParamMarked(HashMap<String, Object> params, String coulmn, String[] markeValues) {
        boolean ifMarked = (params.get("ifMarked") != null) && (params.get("ifMarked").equals("true"));
        if (ifMarked) {
            if (markeValues.length > 0) {
                params.put(coulmn + "@in", markeValues);
            } else {
                params.put(coulmn + "@eq", "");
            }
        } else {
            if (markeValues.length > 0) {
                params.put(coulmn + "@notIn", markeValues);
            }
        }
        params.remove("ifMarked");
        return params;
    }

    // 获取userName
    private String getUserName(String accessKeyId) throws Exception {
        if (this.userNameMap.get(accessKeyId) == null) {
            AliAccount aliAccount = new AliAccountDaoImpl().getAliAccount(accessKeyId);
            this.userNameMap.put(accessKeyId, aliAccount.getUserName());
            return aliAccount.getUserName();
        } else {
            return this.userNameMap.get(accessKeyId);
        }
    }

}
