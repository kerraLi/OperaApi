package com.ywxt.Service.Ali;


import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.cdn.model.v20141111.DescribeRefreshTasksResponse;
import com.aliyuncs.bssopenapi.model.v20171214.QueryAccountBalanceResponse;
import com.ywxt.Domain.Ali.AliEcs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface AliService {

    // 获取账户余额
    public abstract QueryAccountBalanceResponse.Data getAccountBalance() throws Exception;

}
