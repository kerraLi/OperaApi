package com.ywxt.Service.Ali;


import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.cdn.model.v20141111.DescribeRefreshTasksResponse;
import com.aliyuncs.cdn.model.v20141111.DescribeUserDomainsResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeInstanceStatusResponse;
import com.aliyuncs.bssopenapi.model.v20171214.QueryAccountBalanceResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import com.ywxt.Domain.AliCdn;
import com.ywxt.Domain.AliEcs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface AliService {

    // 获取账户余额
    public abstract QueryAccountBalanceResponse.Data getAccountBalance() throws Exception;

    // ECS:获取服务器列表
    public abstract List<AliEcs> getEcsList(HashMap<String, Object> params) throws Exception;

    // ECS：获取服务器列表&分页
    public abstract JSONObject getEcsListPage(HashMap<String, Object> params, int pageNumber, int pageSize) throws Exception;

    // ECS：服务器启动
    public abstract void startEcs(String regionId, String instanceId) throws Exception;

    // ECS：服务器停止
    public abstract void stopEcs(String regionId, String instanceId, boolean forceStop) throws Exception;

    // ECS：服务器重启
    public abstract void restartEcs(String regionId, String instanceId, boolean forceStop) throws Exception;

    // ECS：服务器释放
    public abstract void deleteEcs(String regionId, String instanceId, boolean forceStop) throws Exception;

    // CDN：域名列表
    public abstract List<AliCdn> getCdnDomainList(HashMap<String, Object> params, int pageNumber, int pageSize) throws Exception;

    // CDN：刷新CDN
    public abstract Map<String, String> refreshCdnObjectCaches(String objectPath, String objectType) throws Exception;

    // CDN：刷新任务查看
    public abstract List<DescribeRefreshTasksResponse.CDNTask> getCdnRefreshTask(String taskId) throws Exception;
}
