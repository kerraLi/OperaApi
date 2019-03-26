package com.ywxt.Service.Ali;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.cdn.model.v20141111.DescribeRefreshTasksResponse;
import com.ywxt.Domain.Ali.AliCdn;
import com.ywxt.Domain.Ali.AliCdnTask;
import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface AliCdnService {

    // CDN：查询报警
    List<AliCdn> getAlertList() throws Exception;

    // CDN：列表
    Page<AliCdn> getList(Map<String, String> params) throws Exception;

    // CDN：刷新CDN
    Map<String, String> refreshCdn(String operateType, String refreshType, String objectPath) throws Exception;

    // CDN-TASK：列表
    Page<AliCdnTask> getTaskList(Map<String, String> params) throws Exception;

    // CDN-TASK：刷新任务
    AliCdnTask updateTask(int id) throws Exception;

}
