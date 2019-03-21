package com.ywxt.Service.Godaddy;

public interface GodaddyService {

    // 更新源数据
    void freshSourceData(String keyId, String keySecret) throws Exception;

    // 删除源数据
    void removeSourceData(String keyId);

    // 获取userName
    String getUserName(String accessKeyId) throws Exception;

}
