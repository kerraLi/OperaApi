package com.ywxt.Service.Ali;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.cdn.model.v20141111.DescribeRefreshTasksResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface AliCdnService {

    // CDN：域名列表
    public abstract JSONObject getCdnDomainList(HashMap<String, Object> params, int pageNumber, int pageSize) throws Exception;

    // CDN：刷新CDN
//    public abstract Map<String, String> refreshCdn(String objectPath, String objectType) throws Exception;

    // CDN：刷新任务查看
    public abstract List<DescribeRefreshTasksResponse.CDNTask> getCdnRefreshTask(String taskId) throws Exception;
}
