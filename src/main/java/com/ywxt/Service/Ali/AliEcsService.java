package com.ywxt.Service.Ali;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Domain.Ali.AliEcs;

import java.util.HashMap;
import java.util.List;

public interface AliEcsService {

    // ECS:获取服务器列表
    public abstract List<AliEcs> getEcsList(HashMap<String, Object> params) throws Exception;

    // ECS：获取服务器列表&分页
    public abstract JSONObject getEcsList(HashMap<String, Object> params, int pageNumber, int pageSize) throws Exception;

    // ECS：服务器启动
    public abstract void startEcs(String regionId, String instanceId) throws Exception;

    // ECS：服务器停止
    public abstract void stopEcs(String regionId, String instanceId, boolean forceStop) throws Exception;

    // ECS：服务器重启
    public abstract void restartEcs(String regionId, String instanceId, boolean forceStop) throws Exception;

    // ECS：服务器释放
    public abstract void deleteEcs(String regionId, String instanceId, boolean forceStop) throws Exception;

}
