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

/*
* 查询所有角色信息
* */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ResponseBody
   public List<Role> list(){
      List<Role>roles= roleService.findAll();
      return roles;
   }

/*
* 增加角色
* */
   @RequestMapping(value = "/add",method = RequestMethod.POST)
   @PassToken
public void add(@RequestBody(required = false) Role role){
    roleService.save(role);

}

/*
* 修改角色信息
* */
    @RequestMapping("/update")
    public void update(@RequestParam Long id){
        roleService.update(id);
}

/*
* 根据id删除角色
* */
@RequestMapping("/delete")
    public void deleteById(@RequestParam Long id){
        roleService.deleteById(id);
}

/*
* 根据id查询角色
* */
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    @ResponseBody
    public Role findRoleById(@PathVariable(value = "id")long id){
        Role role = roleService.findRoleById(id);
        return role;
    }
}