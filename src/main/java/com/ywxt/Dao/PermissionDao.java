package com.ywxt.Dao;

import com.ywxt.Domain.Permission;

import java.util.List;

public interface PermissionDao {

    List<Permission> list();

    List<String> findAllUrl();

    void update(Permission permission);

    void save(Permission permission);

    void deleteById(Long id);

    Permission findByUrl(String url);

    Permission findPermissionById(Long id);


}
