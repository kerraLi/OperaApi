package com.ywxt.Service.Resource.Impl;


import com.alibaba.fastjson.JSONObject;
import com.ywxt.Dao.Resource.CategoryDao;
import com.ywxt.Domain.Resource.Category;
import com.ywxt.Service.Resource.CategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private CategoryDao categoryDao;

    public int create(Category category) throws Exception {
        // 非根目录
        if (category.getParentId() > 0) {
            Category parentCategory = this.getCategory(category.getParentId());
            // todo 如该项已有数据，无法增加
        }
        return categoryDao.create(category);
    }

    public boolean remove(int id) throws Exception {
        Category category = this.getCategory(id);
        // 如该类有子分类无法删除
        List<Category> children = categoryDao.getList(new HashMap<>() {{
            put("parentId", category.getId());
        }});
        if (children.size() > 0) {
            throw new Exception("该分类下还有其他分类，删除失败。");
        }
        // todo 如该项已有数据，无法删除
        return categoryDao.delete(id);
    }

    public Category update(Category category) {
        return categoryDao.update(category);
    }

    public Category getCategory(int id) throws Exception {
        Category category = categoryDao.getCategory(id);
        if (category == null) {
            throw new Exception("资源分类不存在");
        }
        return category;
    }

    public Category getCategory(String path) throws Exception {
        Category category = categoryDao.getCategory(path);
        if (category == null) {
            throw new Exception("资源分类不存在");
        }
        return category;
    }

    public Category save(Category category) throws Exception {
        if (category.getId() == null) {
            int id = this.create(category);
            return this.getCategory(id);
        } else {
            return this.update(category);
        }
    }

    public List<Category> getList(HashMap<String, Object> params) {
        return categoryDao.getList(params);
    }
}
