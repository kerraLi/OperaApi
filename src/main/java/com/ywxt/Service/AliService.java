package com.ywxt.Service;


import com.aliyuncs.cdn.model.v20141111.DescribeRefreshTasksResponse;
import com.aliyuncs.cdn.model.v20141111.DescribeUserDomainsResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeInstanceStatusResponse;
import com.aliyuncs.bssopenapi.model.v20171214.QueryAccountBalanceResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;

import java.util.List;
import java.util.Map;

public interface AliService {

    // 获取账户余额
    public abstract QueryAccountBalanceResponse.Data getAccountBalance() throws Exception;

    // ECS：获取服务器列表
    public abstract List<DescribeInstancesResponse.Instance> getEcsList(Integer pageSize, Integer pageNumber) throws Exception;

    // ECS：获取服务器状态列表
    public abstract List<DescribeInstanceStatusResponse.InstanceStatus> getEcsStatusList(Integer pageSize, Integer pageNumber) throws Exception;

    // ECS：服务器启动
    public abstract void startEcs(String regionId, String instanceId) throws Exception;

    // ECS：服务器停止
    public abstract void stopEcs(String regionId, String instanceId, boolean forceStop) throws Exception;

    // ECS：服务器重启
    public abstract void restartEcs(String regionId, String instanceId, boolean forceStop) throws Exception;

    // ECS：服务器释放
    public abstract void deleteEcs(String regionId, String instanceId, boolean forceStop) throws Exception;

    // CDN：域名列表
    public abstract List<DescribeUserDomainsResponse.PageData> getCdnDomainList(Integer pageSize, Integer pageNumber) throws Exception;

    // CDN：刷新CDN
    public abstract Map<String, String> refreshCdnObjectCaches(String objectPath, String objectType) throws Exception;

    // CDN：刷新任务查看
    public abstract List<DescribeRefreshTasksResponse.CDNTask> getCdnRefreshTask(String taskId) throws Exception;
}
