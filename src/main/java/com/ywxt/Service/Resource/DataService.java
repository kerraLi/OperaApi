package com.ywxt.Service.Resource;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ywxt.Domain.Resource.Data;

import java.util.HashMap;
import java.util.List;

public interface DataService {
    public void upload(int categoryId, List<JSONArray> list) throws Exception;

    public int create(Data data) throws Exception;

    public boolean remove(int id) throws Exception;

    public boolean removeAll(Integer[] ids) throws Exception;

    public Data update(Data data);

    public Data getData(int id);

    public Data save(Data data) throws Exception;

    public JSONObject getList(HashMap<String, Object> params, int pageNumber, int pageSize) throws Exception;
}
