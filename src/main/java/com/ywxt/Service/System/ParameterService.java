package com.ywxt.Service.System;

import com.ywxt.Domain.System.Parameter;

import java.util.List;

public interface ParameterService {

    // 获取value
    String getValue(String key);

    // 获取列表
    List<Parameter> getList() throws Exception;

    // 修改value
    void updateValue(int id, String value);

    // 创建key-value
    Parameter createKeyValue(String key, String value) throws Exception;

    // 创建key-value-introduce
    Parameter createKeyValue(String key, String value, String introduce) throws Exception;

    // 新增&修改
    void save(Parameter parameter);

    // 删除
    void delete(int id);

    // 删除
    void delete(Parameter parameter);
}
