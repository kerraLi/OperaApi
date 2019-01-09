package com.ywxt.Controller.Godaddy;

import com.ywxt.Controller.CommonController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/go")
public class GodaddyController extends CommonController {

    @ResponseBody
    @RequestMapping(value = {"/domain/list"}, method = RequestMethod.POST)
    public void domainList(HttpServletRequest request, HttpServletResponse response) throws Exception {


    }
}
