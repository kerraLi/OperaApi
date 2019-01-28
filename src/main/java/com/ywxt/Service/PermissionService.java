package com.ywxt.Service;

import com.ywxt.Domain.Permission;

import java.util.List;


public interface PermissionService {
//    查询所用权限
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


/*
* 通过权限id查询权限
* */
  Permission  findPermissionById(Long id);

    List<String> findAllUrl();

/*
* 更新权限信息
* */
    void update(Permission permission);
}
