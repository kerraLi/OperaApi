package com.ywxt.Controller;

import com.ywxt.Service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RoleController extends CommonController {
    @Autowired
    private RoleService roleService;
    public void full(HttpRequest request){}

}