package com.ywxt.Controller.Ali;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Annotation.NotOperationAction;
import com.ywxt.Controller.CommonController;
import com.ywxt.Domain.Ali.AliAccount;
import com.ywxt.Service.Ali.Impl.AliAccountServiceImpl;
import com.ywxt.Service.Impl.ParameterIgnoreServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping(value = "/ali/account", name = "阿里云账号")
public class AliAccountController extends CommonController {

    @NotOperationAction
    @RequestMapping(value = {"/list"}, name = "列表", method = RequestMethod.GET)
    @ResponseBody
    public List<AliAccount> list(HttpServletRequest request) throws Exception {
        return new AliAccountServiceImpl().getList(true);
    }

    @RequestMapping(value = {"/save"}, name = "修改", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject save(@ModelAttribute AliAccount aliAccount) throws Exception {
        new AliAccountServiceImpl().saveAliAccount(aliAccount);
        return this.returnObject(new HashMap<String, Object>() {{
        }});
    }

    @RequestMapping(value = {"/delete/{id}"}, name = "删除", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject delete(HttpServletRequest request, @PathVariable Integer id) throws Exception {
        if (new AliAccountServiceImpl().deleteAccount(id)) {
            return this.returnObject(new HashMap<String, Object>() {{
            }});
        }
        throw new Exception("删除失败。");
    }

    // 设置mark
    @ResponseBody
    @RequestMapping(value = {"/param/{status}/{id}"}, name = "标记", method = RequestMethod.POST)
    public JSONObject ecsParamSet(@PathVariable String status, @PathVariable Integer id) throws Exception {
        AliAccount aliAccount = new AliAccountServiceImpl().getAliAccount(id);
        if (status.equals("mark")) {
            new ParameterIgnoreServiceImpl().saveMarked(aliAccount);
        } else if (status.equals("unmark")) {
            new ParameterIgnoreServiceImpl().deleteMarked(aliAccount);
        }
        return this.returnObject(new HashMap<String, Object>() {{
        }});
    }
}
