package com.ywxt.Service.Aws;

public interface AwsService {

    // 更新源数据
    void freshSourceData(String keyId, String keySecret) throws Exception;

    // 删除源数据
    void removeSourceData(String keyId);

    // 获取userName
    String getUserName(String accessKeyId);
}
