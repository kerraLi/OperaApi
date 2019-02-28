package com.ywxt.Service.Resource.Impl;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Dao.Resource.Impl.HardwareDaoImpl;
import com.ywxt.Domain.Resource.Hardware;

import java.util.*;

public class HardwareServiceImpl {

    // JSONObject
    public JSONObject getList(HashMap<String, Object> params, int pageNumber, int pageSize) throws Exception {
        List<Hardware> list = new HardwareDaoImpl().getList(params, pageNumber, pageSize);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", new HardwareDaoImpl().getTotal(params));
        jsonObject.put("items", list);
        return jsonObject;
    }

    // 保存 || 新增
    public int save(Hardware hardware) throws Exception {
        return new HardwareDaoImpl().save(hardware);
    }

    // 批量保存
    public void saveAll(List<Hardware> list) throws Exception {
        new HardwareDaoImpl().saveAll(list);
    }

    // 删除
    public void delete(int id) throws Exception {
        new HardwareDaoImpl().delete(id);
    }

    // 批量删除
    public void deleteAll(Integer[] ids) throws Exception {
        List<Integer> idList = new ArrayList<Integer>(Arrays.asList(ids));
        new HardwareDaoImpl().deleteAll(idList);
    }
}
