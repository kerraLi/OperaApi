package com.ywxt.Service.System;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Domain.Log.LogRefresh;

import java.util.List;

public interface RefreshService {

    // 获取刷新类型
    List<JSONObject> refreshTypes();

    // 数据刷新
    void refreshData(String type);

    // 保存刷新日志
    void saveRefreshLog(LogRefresh logRefresh);
}
