package com.ywxt.Domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.util.*;


public class Permission implements Serializable {


    private Long id;
    private Permission parentPermission;//当前权限的上级权限
    private String permissionName;
    private String url;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Permission)) return false;
        Permission that = (Permission) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    /*  @Override
                public boolean equals(Object o) {
                    if (this == o) return true;
                    if (o == null || getClass() != o.getClass()) return false;
                    Permission that = (Permission) o;
                    if (id == null) {
                        return that.id == null;
                    } else return id.equals(that.id);
                }
                @Override
                public int hashCode() {
                    return Objects.hash(id);
                }*/
    private String generatemenu;//是否生成菜单  1:生成  0：不生成
    private Long zindex;

    public Permission() {
    }


    public void add(Permission permission){
        if(null == this.children){
            this.children = new HashSet<>();
        }
        this.children.add(permission);
    }

    public String getGeneratemenu() {
        return generatemenu;
    }
    public void setGeneratemenu(String generatemenu) {
        this.generatemenu = generatemenu;
    }

    private Set<Permission> children=new HashSet<>(0);
//一个权限可以授予多个角色，权限和角色之间属于一对多关系，彼此之间属于多对多关系

    private Set<Role>roles=new HashSet<>(0);


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    @JsonIgnore
    public Permission getParentPermission() {
        return parentPermission;
    }

    public void setParentPermission(Permission parentPermission) {
        this.parentPermission = parentPermission;
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

    public Long getZindex() {
        return zindex;
    }

    public void setZindex(Long zindex) {
        this.zindex = zindex;
    }
 //   @JsonIgnore
    public Set<Permission> getChildren() {
        return children;
    }

    public void setChildren(Set<Permission> children) {
        this.children = children;
    }
    @JsonIgnore
    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }


}
