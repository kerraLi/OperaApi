package com.ywxt.Dao;

import com.ywxt.Domain.Permission;

import java.util.List;

public interface PermissionDao {

    List<Permission> list();

    List<String> findAllUrl();

    void update(Permission permission);

    int add(Permission permission);

    void deleteById(long id);
}
