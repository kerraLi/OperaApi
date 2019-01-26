package com.ywxt.Service;

import com.ywxt.Domain.Permission;
import com.ywxt.Utils.PageBean;

import java.util.List;


public interface PermissionService {

    public List<Permission> findAll();


    /**
     * 查询所有权限信息
     * @return
     */
    List<Permission> list();

    /**
     * 添加权限资源
     * @param permission
     * @return
     */
    int add(Permission permission);

    public void pageQuery(PageBean pageBean);

  Permission  findPermissionById(Long id);

    List<String> findAllUrl();

    /*  public findPermissionByUser()*/
}
