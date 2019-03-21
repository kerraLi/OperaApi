package com.ywxt.Service.Ali;


import com.aliyuncs.IAcsClient;
import com.aliyuncs.bssopenapi.model.v20171214.QueryAccountBalanceResponse;
import com.ywxt.Domain.Ali.AliAccount;
import com.ywxt.Domain.Ali.AliCdn;
import com.ywxt.Domain.Ali.AliEcs;

import java.util.*;

public interface AliService {

    //获取阿里连接
    public IAcsClient getAliClient(String regionId, AliAccount aliAccount);

    public IAcsClient getAliClient(String regionId, String keyId, String keySecret);

    // 获取ali-key
    public Map<String, String> getKey(AliCdn aliCdn) throws Exception;

    public Map<String, String> getKey(AliEcs aliEcs) throws Exception;

    // 获取userName
    public String getUserName(String accessKeyId) throws Exception;

    // 获取accessKeySecret
    public String getAccessKeySecret(String accessKeyId) throws Exception;

    // 删除账号对应数据源
    public void removeSourceData(String keyId);

    // 更新ali源数据(按账户更新)
    public void freshSourceData(String keyId, String keySecret) throws Exception;
}
