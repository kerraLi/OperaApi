package com.ywxt.Controller.System;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Annotation.NotOperationAction;
import com.ywxt.Controller.CommonController;
import com.ywxt.Domain.System.Parameter;
import com.ywxt.Service.System.Impl.ParameterServiceImpl;
import com.ywxt.Service.System.ParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping(value = "/parameter", name = "系统参数")
public class ParameterController extends CommonController {


    @Autowired
    private ParameterService parameterService;

    // 列表
    @NotOperationAction
    @RequestMapping(value = {"/list"}, name = "列表", method = RequestMethod.POST)
    @ResponseBody
    public List<Parameter> parameterList() throws Exception {
        return parameterService.getList();
    }

    // 修改value
    @RequestMapping(value = {"/update/{id}"}, name = "修改", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject updateValue(@PathVariable Integer id, @NotBlank String value) {
        parameterService.updateValue(id, value);
        return this.returnObject();
    }

    // 新增参数
    @RequestMapping(value = {"/create"}, name = "新增", method = RequestMethod.POST)
    @ResponseBody
    public Parameter createParameter(@NotBlank String key, @NotBlank String value, String introduce) throws Exception {
        return parameterService.createKeyValue(key, value, introduce);
    }

    // 删除参数
    @RequestMapping(value = {"/delete/{id}"}, name = "删除", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject deleteParameter(@PathVariable Integer id) {
        parameterService.delete(id);
        return this.returnObject();
    }
}
