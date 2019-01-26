package com.ywxt.Service.Impl;

import com.ywxt.Dao.Impl.ParameterDaoImpl;
import com.ywxt.Domain.Parameter;
import com.ywxt.Service.ParameterService;

import java.util.HashMap;
import java.util.List;

public class ParameterServiceImpl implements ParameterService {

    // 获取value
    public String getValue(String key) throws Exception {
        Parameter parameter = new ParameterDaoImpl().getParameter(key);
        return parameter.getValue();
    }

    // 获取列表
    public List<Parameter> getList(HashMap<String, Object> params) throws Exception {
        return new ParameterDaoImpl().getList(params);
    }

    // 修改value
    public int updateValue(int id, String value) throws Exception {
        Parameter parameter = new ParameterDaoImpl().getParameter(id);
        parameter.setValue(value);
        return this.save(parameter);
    }

    // 创建key-value
    public Parameter createKeyValue(String key, String value) throws Exception {
        Parameter parameter = new ParameterDaoImpl().getParameter(key);
        if (parameter != null) {
            throw new Exception("该KEY值已存在，请更换后重试。");
        }
        parameter = new Parameter();
        parameter.setKey(key);
        parameter.setValue(value);
        parameter.setStatus("temp");
        return new ParameterDaoImpl().getParameter(this.save(parameter));
    }

    // 新增&修改
    public int save(Parameter parameter) throws Exception {
        return new ParameterDaoImpl().save(parameter);
    }

    // 删除
    public void delete(int id) throws Exception {
        Parameter parameter = new ParameterDaoImpl().getParameter(id);
        this.delete(parameter);
    }

    // 删除
    public void delete(Parameter parameter) throws Exception {
        if (parameter.getStatus().equals("fixed")) {
            throw new Exception("删除失败。该参数为常驻参数，删除将会影响平台运行。");
        }
        new ParameterDaoImpl().delete(parameter.getId());
    }
}
