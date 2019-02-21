package com.ywxt.Service.Resource;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Domain.Resource.Category;

import java.util.HashMap;
import java.util.List;

/**
 * 说明：
 * ---------新增----------
 * 没有数据的分类项可以增加子分类；如该项已有数据，需要清空数据，然后增加子分类；
 * ---------修改----------
 * 修改type类型：若有数据修改失败
 * ---------删除----------
 * 其下没有数据，且没有分类则可以删除
 */
public interface CategoryService {
    public int create(Category category) throws Exception;

    public boolean remove(int id) throws Exception;

    public Category update(Category category) throws Exception;

    public Category getCategory(int id) throws Exception;

    public Category getCategory(String path) throws Exception;

    public Category save(Category category) throws Exception;

    public List<Category> getList(HashMap<String, Object> params);

    // 判断分类下是否包含资源
    public boolean hasData(int categoryId);

    // 判断分类下是否包含子分类
    public boolean hasChildren(int categoryId);
}
