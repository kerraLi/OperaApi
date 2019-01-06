package com.ywxt.Controller.Ali;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Controller.CommonController;
import com.ywxt.Domain.AliAccount;
import com.ywxt.Service.Ali.Impl.AliAccountServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/aliaccount")
public class AliAccountController extends CommonController {

    @RequestMapping(value = {"/list"}, method = RequestMethod.GET)
    @ResponseBody
    public List<AliAccount> list(HttpServletRequest request) {
        return new AliAccountServiceImpl().getList();
    }

    // todo save
    // public sa


    @RequestMapping(value = {"/delete/{id}"}, method = RequestMethod.POST)
    @ResponseBody
    public JSONObject delete(HttpServletRequest request, @PathVariable Integer id) throws Exception {
        if (new AliAccountServiceImpl().deleteAccount(id)) {
            return this.returnObject(null);
        }
        throw new Exception("删除失败。");
    }
}
