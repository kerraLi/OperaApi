package com.ywxt.Controller;

import com.ywxt.Annotation.PassToken;
import com.ywxt.Domain.Role;
import com.ywxt.Domain.User;
import com.ywxt.Service.RoleService;
import com.ywxt.Utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


@RestController
@RequestMapping("/role")
public class RoleController extends CommonController {




    @Autowired
    private RoleService roleService;


    @RequestMapping(value = {"/info"}, method = RequestMethod.GET)
    @ResponseBody
    public User info(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return this.getUserFromAuthToken(request);
    }


    @RequestMapping("/list")
   public List<Role> list(){
      List<Role>roles= roleService.findAll();
      return roles;
   }


   @RequestMapping("/add")
   @PassToken
public void add(@RequestBody Role role){
    roleService.save(role);

}


@RequestMapping("/update")
    public void update(Role role){
        roleService.update(role);
}

}