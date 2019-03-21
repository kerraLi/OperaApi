package com.ywxt.Service.Ali;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Domain.Ali.AliEcs;
import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface AliEcsService {
    // ecs-获取单个
    AliEcs getEcs(int id);

    // ecs-查询报警
    List<AliEcs> getAlertList() throws Exception;

    // ecs-查询所有实例的详细信息&分页
    Page<AliEcs> getList(Map<String, String> params) throws Exception;

    // ecs-最新状态
    AliEcs updateEcsStatus(AliEcs aliEcs) throws Exception;

    // ecs-预付费
    AliEcs perPay(String instanceId, String periodUnit, int period) throws Exception;

    // ecs-更新单个ecs数据
    AliEcs updateEcs(String instanceId) throws Exception;

    // ecs-操作
    void actionEcs(String action, int id) throws Exception;

}
