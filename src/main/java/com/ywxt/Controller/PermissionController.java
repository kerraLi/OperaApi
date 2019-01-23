package com.ywxt.Controller;

import com.ywxt.Service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/permission")
public class PermissionController extends CommonController{
@Autowired
    private PermissionService permissionService;

public void add(){}

public void update(){}

public void delete(){}

public void findPermissionById(Long id){}

}
