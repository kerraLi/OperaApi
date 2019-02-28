package com.ywxt.Service.Resource.Impl;


import com.ywxt.Dao.Resource.CategoryDao;
import com.ywxt.Dao.Resource.DataDao;
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
    @Resource
    private DataDao dataDao;

    public int create(Category category) throws Exception {
        // 如该项已有数据，无法增加子分类
        if (category.getParentId() > 0) {
            if (this.hasData(category.getParentId())) {
                throw new Exception("该父分类下已有数据，无法增加子分类。");
            }
        }
        return categoryDao.create(category);
    }

    public boolean remove(int id) throws Exception {
        Category category = this.getCategory(id);
        // 如该类有子分类无法删除
        if (this.hasChildren(category.getId())) {
            throw new Exception("该分类下还有其他分类，删除失败。");
        }
        // 如该项已有数据，无法删除
        if (this.hasData(category.getId())) {
            throw new Exception("该分类下已有数据，删除失败。");
        }
        return categoryDao.delete(id);
    }

    public Category update(Category category) throws Exception {
        Category oldCate = this.getCategory(category.getId());
        // 如该项已有数据，无法修改类型
        if (!oldCate.getType().equals(category.getType())) {
            if (this.hasData(category.getId())) {
                throw new Exception("该分类下已有数据，无法修改资源类型。");
            }
        }
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


    // 判断分类下是否包含资源
    public boolean hasData(int categoryId) {
        int total = dataDao.getListTotal(new HashMap<>() {{
            put("categoryId", categoryId);
        }});
        return total > 0;
    }

    // 判断分类下是否包含子分类
    public boolean hasChildren(int categoryId) {
        List<Category> children = categoryDao.getList(new HashMap<>() {{
            put("parentId", categoryId);
        }});
        return children.size() > 0;
    }
}
