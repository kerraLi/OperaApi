package com.ywxt.Controller.Godaddy;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Annotation.NotOperationAction;
import com.ywxt.Controller.CommonController;
import com.ywxt.Domain.Godaddy.GodaddyAccount;
import com.ywxt.Service.Godaddy.Impl.GodaddyAccountServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/go/account")
public class GodaddyAccountController extends CommonController {

    @NotOperationAction
    @RequestMapping(value = {"/list"}, method = RequestMethod.GET)
    @ResponseBody
    public List<GodaddyAccount> list(HttpServletRequest request) throws Exception {
        return new GodaddyAccountServiceImpl().getList();
    }

    @RequestMapping(value = {"/save"}, method = RequestMethod.POST)
    @ResponseBody
    public JSONObject save(@ModelAttribute GodaddyAccount godaddyAccount) throws Exception {
        new GodaddyAccountServiceImpl().saveAccount(godaddyAccount);
        return this.returnObject(new HashMap<String, Object>() {{
        }});
    }

    @RequestMapping(value = {"/delete/{id}"}, method = RequestMethod.POST)
    @ResponseBody
    public JSONObject delete(HttpServletRequest request, @PathVariable Integer id) throws Exception {
        if (new GodaddyAccountServiceImpl().deleteAccount(id)) {
            return this.returnObject(new HashMap<String, Object>() {{
            }});
        }
        throw new Exception("删除失败。");
    }
}
