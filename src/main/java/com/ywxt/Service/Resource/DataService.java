package com.ywxt.Service.Resource;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ywxt.Domain.Resource.Data;

import java.util.HashMap;
import java.util.List;

public interface DataService {
    void upload(int categoryId, List<JSONArray> list) throws Exception;

    int create(Data data) throws Exception;

    boolean remove(int id) throws Exception;

    boolean removeAll(Integer[] ids) throws Exception;

    Data update(Data data);

    Data getData(int id);

    Data save(Data data) throws Exception;

    List<Data> getList(HashMap<String, Object> params);

    JSONObject getList(HashMap<String, Object> params, int pageNumber, int pageSize) throws Exception;
}
