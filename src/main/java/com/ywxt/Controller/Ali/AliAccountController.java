package com.ywxt.Controller.Ali;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Controller.CommonController;
import com.ywxt.Domain.AliAccount;
import com.ywxt.Service.Ali.Impl.AliAccountServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/ali/account")
public class AliAccountController extends CommonController {

    @RequestMapping(value = {"/list"}, method = RequestMethod.GET)
    @ResponseBody
    public List<AliAccount> list(HttpServletRequest request) {
        return new AliAccountServiceImpl().getList();
    }

    @RequestMapping(value = {"/save"}, method = RequestMethod.POST)
    @ResponseBody
    public JSONObject save(@ModelAttribute AliAccount aliAccount) {
        new AliAccountServiceImpl().saveAliAccount(aliAccount);
        return this.returnObject(new HashMap<String, Object>() {{
        }});
    }


    @RequestMapping(value = {"/delete/{id}"}, method = RequestMethod.POST)
    @ResponseBody
    public JSONObject delete(HttpServletRequest request, @PathVariable Integer id) throws Exception {
        if (new AliAccountServiceImpl().deleteAccount(id)) {
            return this.returnObject(new HashMap<String, Object>() {{
            }});
        }
        throw new Exception("删除失败。");
    }
}
