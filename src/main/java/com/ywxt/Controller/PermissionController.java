package com.ywxt.Controller;

import com.ywxt.Domain.Permission;
import com.ywxt.Service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/permission")
public class PermissionController extends CommonController{
@Autowired
    private PermissionService permissionService;
@RequestMapping("/add")
public void add(Permission permission){
    permissionService.add(permission);
}
@RequestMapping("/update")
public void update(Permission permission){
    permissionService.update(permission);
}
@RequestMapping("/delete")
public void deleteById(Long id){}

@RequestMapping("/findByPermissionId")
public void findPermissionById(Long id){
    permissionService.findPermissionById(id);
}

}
