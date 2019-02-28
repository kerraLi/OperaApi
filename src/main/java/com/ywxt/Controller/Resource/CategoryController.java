package com.ywxt.Controller.Resource;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Annotation.NotOperationAction;
import com.ywxt.Annotation.PassToken;
import com.ywxt.Controller.CommonController;
import com.ywxt.Domain.Resource.Category;
import com.ywxt.Service.Resource.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/resource/category")
public class CategoryController extends CommonController {

    @Resource
    private CategoryService categoryService;

    @NotOperationAction
    @ResponseBody
    @RequestMapping(value = {"/info/{path}"}, method = RequestMethod.GET)
    public Category info(@PathVariable String path) throws Exception {
        return categoryService.getCategory(path);
    }

    @NotOperationAction
    @ResponseBody
    @RequestMapping(value = {"/list"}, method = RequestMethod.GET)
    public List<Category> list(HttpServletRequest request) throws Exception {
        HashMap<String, Object> params = new HashMap<String, Object>();
        return categoryService.getList(params);
    }

    @ResponseBody
    @RequestMapping(value = {"/save"}, method = RequestMethod.POST)
    public JSONObject save(@ModelAttribute Category category) throws Exception {
        if (category.getPath().isEmpty()) {
            throw new Exception("分类编码不能为空。");
        }
        categoryService.save(category);
        return this.returnObject(new HashMap<String, Object>() {{
        }});
    }

    @ResponseBody
    @RequestMapping(value = {"/delete/{id}"}, method = RequestMethod.POST)
    public JSONObject delete(@PathVariable Integer id) throws Exception {
        if (categoryService.remove(id)) {
            return this.returnObject(new HashMap<String, Object>() {{
            }});
        }
        throw new Exception("删除失败。");
    }
}
