package com.ywxt.Dao.Resource;

import com.ywxt.Domain.Resource.Category;

import java.util.HashMap;
import java.util.List;

public interface CategoryDao {
    public int create(Category category);

    public boolean delete(int id);

    public boolean delete(Category category);

    public Category update(Category category);

    public Category getCategory(int id);

    public Category getCategory(String path);

    public List<Category> getList(HashMap<String, Object> params);

    public int getListTotal(HashMap<String, Object> params);
}
