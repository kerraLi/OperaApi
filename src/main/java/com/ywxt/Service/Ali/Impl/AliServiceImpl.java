package com.ywxt.Service.Ali.Impl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.bssopenapi.model.v20171214.QueryAccountBalanceRequest;
import com.aliyuncs.bssopenapi.model.v20171214.QueryAccountBalanceResponse;
import com.aliyuncs.cdn.model.v20141111.*;
import com.aliyuncs.ecs.model.v20140526.*;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.ywxt.Dao.Impl.AliEcsDaoImpl;
import com.ywxt.Domain.AliAccount;
import com.ywxt.Domain.AliEcs;
import com.ywxt.Enum.AliRegion;
import com.ywxt.Service.Ali.AliService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AliServiceImpl implements AliService {

    private String accessKeyId;
    private String accessKeySecret;

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
    public void getRegion() throws Exception {
        IClientProfile profile = DefaultProfile.getProfile("", this.accessKeyId, this.accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
    }

    // 更新ali源数据(按账户更新)
    public void freshSourceData() throws Exception {
        // 更新ecs数据
        new AliEcsDaoImpl().deleteAliEcsByAccessId(this.accessKeyId);
        List<AliEcs> aeList = new ArrayList<>();
        for (AliRegion e : AliRegion.values()) {
            IClientProfile profile = DefaultProfile.getProfile(e.getRegion(), this.accessKeyId, this.accessKeySecret);
            IAcsClient client = new DefaultAcsClient(profile);
            int pageNumber = 1;
            int pageSize = 20;
            while (true) {
                DescribeInstancesRequest describe = new DescribeInstancesRequest();
                describe.setPageSize(pageSize);
                describe.setPageNumber(pageNumber);
                DescribeInstancesResponse response = client.getAcsResponse(describe);
                // aeList
                for (DescribeInstancesResponse.Instance i : response.getInstances()) {
                    AliEcs ecs = new AliEcs(this.accessKeyId, i);
                    aeList.add(ecs);
                }
                // 最后一页 跳出
                if (response.getInstances().size() < pageSize) {
                    break;
                }
                pageNumber++;
            }
        }
        new AliEcsDaoImpl().saveAliEcses(aeList);
        // 更新cdn域名数据
    }

    // ecs-查询所有实例的详细信息
    // ** 包含所有区域及分页信息
    public List<DescribeInstancesResponse.Instance> getEcsList() throws Exception {
        List<DescribeInstancesResponse.Instance> isList = new ArrayList<>();
//        for (AliRegion e : AliRegion.values()) {
////            IClientProfile profile = DefaultProfile.getProfile(e.getRegion(), this.accessKeyId, this.accessKeySecret);
////            IAcsClient client = new DefaultAcsClient(profile);
////            int pageNumber = 1;
////            while (true) {
////                DescribeInstancesRequest describe = new DescribeInstancesRequest();
////                describe.setPageSize(this.pageSize);
////                describe.setPageNumber(pageNumber);
////                DescribeInstancesResponse response = client.getAcsResponse(describe);
//////                if()
////                isList.addAll(response.getInstances());
////            }
////
////        }
        return isList;
    }

    // ecs-批量获取当前用户所有实例的状态信息
    public List<DescribeInstanceStatusResponse.InstanceStatus> getEcsStatusList() throws Exception {
        List<DescribeInstanceStatusResponse.InstanceStatus> isList = new ArrayList<>();
        for (AliRegion e : AliRegion.values()) {
            IClientProfile profile = DefaultProfile.getProfile(e.getRegion(), this.accessKeyId, this.accessKeySecret);
            IAcsClient client = new DefaultAcsClient(profile);
            DescribeInstanceStatusRequest describe = new DescribeInstanceStatusRequest();
            DescribeInstanceStatusResponse response = client.getAcsResponse(describe);
            isList.addAll(response.getInstanceStatuses());
        }
        return isList;
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

    // CDN-域名列表
    // ** 包含所有分页信息
    public List<DescribeUserDomainsResponse.PageData> getCdnDomainList() throws Exception {
        IClientProfile profile = DefaultProfile.getProfile(AliRegion.QINGDAO.getRegion(), this.accessKeyId, this.accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        DescribeUserDomainsRequest request = new DescribeUserDomainsRequest();
        DescribeUserDomainsResponse response = client.getAcsResponse(request);
        return response.getDomains();
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
