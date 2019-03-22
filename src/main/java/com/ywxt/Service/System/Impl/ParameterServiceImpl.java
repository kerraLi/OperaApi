package com.ywxt.Service.System.Impl;

import com.ywxt.Dao.System.ParameterDao;
import com.ywxt.Domain.System.Parameter;
import com.ywxt.Service.System.ParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParameterServiceImpl implements ParameterService {

    @Autowired
    private ParameterDao parameterDao;

    // 获取action-value

    // 获取value
    public String getValue(String key) {
        Parameter parameter = parameterDao.findParameterByKey(key);
        return parameter.getValue();
    }

    // 获取列表
    public List<Parameter> getList() {
        return parameterDao.findAll();
    }

    // 修改value
    public void updateValue(int id, String value) {
        Parameter parameter = parameterDao.findParameterById(id);
        parameter.setValue(value);
        parameterDao.saveAndFlush(parameter);
    }

    // 创建key-value
    public Parameter createKeyValue(String key, String value) throws Exception {
        Parameter parameter = parameterDao.findParameterByKey(key);
        if (parameter != null) {
            throw new Exception("该KEY值已存在，请更换后重试。");
        }
        parameter = new Parameter();
        parameter.setKey(key);
        parameter.setValue(value);
        parameter.setStatus("temp");
        parameterDao.saveAndFlush(parameter);
        return parameter;
    }

    // 创建key-value-introduce
    public Parameter createKeyValue(String key, String value, String introduce) throws Exception {
        Parameter parameter = parameterDao.findParameterByKey(key);
        if (parameter != null) {
            throw new Exception("该KEY值已存在，请更换后重试。");
        }
        parameter = new Parameter();
        parameter.setKey(key);
        parameter.setValue(value);
        parameter.setStatus("temp");
        parameter.setIntroduce(introduce);
        parameterDao.saveAndFlush(parameter);
        return parameter;
    }

    // 新增&修改
    public void save(Parameter parameter) {
        parameterDao.saveAndFlush(parameter);
    }

    // 删除
    public void delete(int id) {
        parameterDao.deleteById(id);
    }

    // 删除
    public void delete(Parameter parameter) {
        parameterDao.delete(parameter);
    }
}
