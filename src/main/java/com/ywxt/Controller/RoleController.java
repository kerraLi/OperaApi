package com.ywxt.Controller;

import com.ywxt.Domain.Role;
import com.ywxt.Service.RoleService;
import com.ywxt.Utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/role")
public class RoleController extends CommonController {
    @Autowired
    private RoleService roleService;
   public List<Role> list(){
      List<Role>roles= roleService.findAll();
      return roles;
   }

}