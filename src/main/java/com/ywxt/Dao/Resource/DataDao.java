package com.ywxt.Dao.Resource;

import com.ywxt.Domain.Resource.Data;

import java.util.HashMap;
import java.util.List;

public interface DataDao {

    public int create(Data data);

    public boolean delete(int id);

    public boolean delete(Data data);

    public Data update(Data data);

    public Data getData(int id);

    public List<Data> getList(HashMap<String, Object> params);

    public List<Data> getList(HashMap<String, Object> params, int pageNumber, int pageSize);

    public int getListTotal(HashMap<String, Object> params);

}
