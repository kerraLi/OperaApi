package com.ywxt.Domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import java.util.ArrayList;

import java.util.List;
import java.util.Objects;


@Entity
public class Permission {
    @Id
    @GeneratedValue
    private Long id;
    private String permissionName;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permission that = (Permission) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
    private String url;

    private Long pid;
    public void add(Permission permission){
        if(null == this.children){
            this.children = new ArrayList<Permission>();
        }
        this.children.add(permission);
    }


    private List<Permission> children ;//当前权限的下级权限集合
    //一个权限可以授予多个角色，权限和角色之间属于一对多关系，彼此之间属于多对多关系
    private List<Role>roles;

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





    public void setUrl(String url) {
        this.url = url;
    }

    public List<Permission> getChildren() {
        return children;
    }

    public void setChildren(List<Permission> children) {
        this.children = children;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
