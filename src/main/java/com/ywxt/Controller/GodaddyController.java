package com.ywxt.Controller;

import com.ywxt.Domain.GodaddyAccount;
import com.ywxt.Utils.Parameter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
@RequestMapping("/go")
public class GodaddyController {

    // 账号列表
    @ResponseBody
    @RequestMapping(value = {"/account/list"}, method = RequestMethod.POST)
    public List<GodaddyAccount> accountList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<GodaddyAccount> gaList = new ArrayList<>();
        for (Map.Entry<String, String> e : Parameter.godaddyAccounts.entrySet()) {
            GodaddyAccount aa = new GodaddyAccount(e.getKey(), e.getValue());
            gaList.add(aa);
        }
        return gaList;
    }

    @ResponseBody
    @RequestMapping(value = {"/domain/list"}, method = RequestMethod.POST)
    public void domainList(HttpServletRequest request, HttpServletResponse response) throws Exception {


    }
}
