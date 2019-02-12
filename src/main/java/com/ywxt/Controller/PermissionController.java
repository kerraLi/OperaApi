package com.ywxt.Controller;

import com.ywxt.Domain.Permission;
import com.ywxt.Service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/permission")
public class PermissionController extends CommonController{
@Autowired
    private PermissionService permissionService;
/*
* 新增权限信息
* */
@RequestMapping(value = "/add",method = RequestMethod.POST)
public void add(@RequestBody Permission permission){
    permissionService.save(permission);
}
/*
* 修改权限信息
* */
@RequestMapping(value = "/update",method = RequestMethod.POST)
public void update(@RequestBody Permission permission){
    permissionService.update(permission);
}
/*
* 根据id删除权限
* */
@RequestMapping(value = "/delete",method = RequestMethod.POST)
public void deleteById(@RequestParam Long id){
    permissionService.deleteById(id);
}
/*
* 通过权限id查询权限
* */
@RequestMapping(value = "/id",method = RequestMethod.GET)
@ResponseBody
public Permission findPermissionById(@RequestParam Long id){
    return permissionService.findPermissionById(id);
}
/*
* 查询所有权限信息
* */
@RequestMapping(value = "/list",method = RequestMethod.GET)
@ResponseBody
    public List<Permission> list(){
    List<Permission> list = permissionService.list();
    return list;
}
}
