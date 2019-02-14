package com.ywxt.Controller;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Annotation.NotOperationAction;
import com.ywxt.Domain.Parameter;
import com.ywxt.Service.Impl.ParameterServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/parameter")
public class ParameterController extends CommonController {


    // 列表
    @NotOperationAction
    @RequestMapping(value = {"/list"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Parameter> parameterList() throws Exception {
        return new ParameterServiceImpl().getList(new HashMap<>() {{
        }});
    }

    // 修改value
    @RequestMapping(value = {"/update/{id}"}, method = RequestMethod.POST)
    @ResponseBody
    public JSONObject updateValue(@PathVariable Integer id, @NotBlank String value) throws Exception {
        new ParameterServiceImpl().updateValue(id, value);
        return this.returnObject(new HashMap<>() {{
        }});
    }

    // 新增参数
    @RequestMapping(value = {"/create"}, method = RequestMethod.POST)
    @ResponseBody
    public Parameter createParameter(@NotBlank String key, @NotBlank String value, String introduce) throws Exception {
        return new ParameterServiceImpl().createKeyValue(key, value, introduce);
    }

    // 删除参数
    @RequestMapping(value = {"/delete/{id}"}, method = RequestMethod.GET)
    @ResponseBody
    public JSONObject deleteParameter(@PathVariable Integer id) throws Exception {
        new ParameterServiceImpl().delete(id);
        return this.returnObject(new HashMap<>() {{
        }});
    }
}
