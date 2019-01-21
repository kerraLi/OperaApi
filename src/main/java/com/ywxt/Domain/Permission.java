package com.ywxt.Domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Permission {
    @Id
    @GeneratedValue
    private Long id;
    private String permissionName;
    private String url;
    private Permission parent;//当前权限的上级权限
    private Set<Permission> children = new HashSet<Permission>();//当前权限的下级权限集合
    //一个权限可以授予多个角色，权限和角色之间属于一对多关系，彼此之间属于多对多关系
    private Set<Role> rols;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getUrl() {
        return url;
    }

    public Permission getParent() {
        return parent;
    }

    public void setParent(Permission parent) {
        this.parent = parent;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Set<Permission> getChildren() {
        return children;
    }

    public void setChildren(Set<Permission> children) {
        this.children = children;
    }

    public Set<Role> getRols() {
        return rols;
    }

    public void setRols(Set<Role> rols) {
        this.rols = rols;
    }
}
