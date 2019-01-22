package com.ywxt.Dao;

import com.ywxt.Domain.Permission;

import java.util.List;

public interface PermissionDao {
    List<Permission> list();
    public int add(Permission permission);
}
