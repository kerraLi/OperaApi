package com.ywxt.Service.Resource.Impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ywxt.Dao.Resource.DataDao;
import com.ywxt.Domain.Resource.Category;
import com.ywxt.Domain.Resource.Data;
import com.ywxt.Service.Resource.CategoryService;
import com.ywxt.Service.Resource.DataService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service("daoService")
public class DataServiceImpl implements DataService {

    @Resource
    private DataDao dataDao;
    @Resource
    private CategoryService categoryService;

    public void upload(int categoryId, List<JSONArray> list) throws Exception {
        Category category = categoryService.getCategory(categoryId);
        for (JSONArray o : list) {
            Data data = new Data();
            data.setCategoryId(categoryId);
            data.setTypeCode(category.getType());
            data.setCreateTime(new Date());
            data.setModifyTime(new Date());
            data.setData(o.toString());
            data.setStatus("normal");
            this.create(data);
        }
    }

    public int create(Data data) throws Exception {
        return dataDao.create(data);
    }

    public boolean remove(int id) throws Exception {
        return dataDao.delete(id);
    }

    public boolean removeAll(Integer[] ids) throws Exception {
        for (int id : ids) {
            this.remove(id);
        }
        return true;
    }

    public Data update(Data data) {
        return dataDao.update(data);
    }

    public Data getData(int id) {
        return dataDao.getData(id);
    }

    public Data save(Data data) throws Exception {
        if (data.getId() == null) {
            int id = this.create(data);
            return this.getData(id);
        } else {
            return this.update(data);
        }
    }

    // 列表
    public JSONObject getList(HashMap<String, Object> params, int pageNumber, int pageSize) throws Exception {
        List<Data> list = dataDao.getList(params, pageNumber, pageSize);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", dataDao.getListTotal(params));
        jsonObject.put("items", list);
        return jsonObject;
    }
}
