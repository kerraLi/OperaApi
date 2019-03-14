package com.ywxt.Controller.Aws;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Annotation.NotOperationAction;
import com.ywxt.Controller.CommonController;
import com.ywxt.Domain.Aws.AwsAccount;
import com.ywxt.Service.Aws.Impl.AwsAccountServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping(value = "/aws/account", name = "亚马逊账户")
public class AwsAccountController extends CommonController {

    @NotOperationAction
    @RequestMapping(value = {"/list"}, name = "列表", method = RequestMethod.GET)
    @ResponseBody
    public List<AwsAccount> list(HttpServletRequest request) throws Exception {
        return new AwsAccountServiceImpl().getList();
    }

    @RequestMapping(value = {"/save"}, name = "修改", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject save(@ModelAttribute AwsAccount awsAccount) throws Exception {
        new AwsAccountServiceImpl().saveAccount(awsAccount);
        return this.returnObject(new HashMap<String, Object>() {{
        }});
    }

    @RequestMapping(value = {"/delete/{id}"}, name = "删除", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject delete(HttpServletRequest request, @PathVariable Integer id) throws Exception {
        if (new AwsAccountServiceImpl().deleteAccount(id)) {
            return this.returnObject(new HashMap<String, Object>() {{
            }});
        }
        throw new Exception("删除失败。");
    }
}
