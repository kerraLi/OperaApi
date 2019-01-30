package com.ywxt.Controller.Aws;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Controller.CommonController;
import com.ywxt.Domain.Aws.AwsAccount;
import com.ywxt.Service.Aws.Impl.AwsAccountServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/aws/account")
public class AwsAccountController extends CommonController {

    @RequestMapping(value = {"/list"}, method = RequestMethod.GET)
    @ResponseBody
    public List<AwsAccount> list(HttpServletRequest request) throws Exception {
        return new AwsAccountServiceImpl().getList();
    }

    @RequestMapping(value = {"/save"}, method = RequestMethod.POST)
    @ResponseBody
    public JSONObject save(@ModelAttribute AwsAccount awsAccount) throws Exception {
        new AwsAccountServiceImpl().saveAccount(awsAccount);
        return this.returnObject(new HashMap<String, Object>() {{
        }});
    }

    @RequestMapping(value = {"/delete/{id}"}, method = RequestMethod.POST)
    @ResponseBody
    public JSONObject delete(HttpServletRequest request, @PathVariable Integer id) throws Exception {
        if (new AwsAccountServiceImpl().deleteAccount(id)) {
            return this.returnObject(new HashMap<String, Object>() {{
            }});
        }
        throw new Exception("删除失败。");
    }
}
