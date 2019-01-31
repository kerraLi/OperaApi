package com.ywxt.Domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import java.io.Serializable;
import java.util.*;

@Entity
public class Permission implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private String permissionName;

  /*  @Override
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

    private Long pid;

    public void add(Permission permission){
        if(null == this.children){
            this.children = new HashSet<>();
        }
        this.children.add(permission);
    }
    @Transient
    private Set<Permission> children;

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public Set<Permission> getChildren() {
        return children;
    }

    public void setChildren(Set<Permission> children) {
        this.children = children;
    }
*/




    private String url;
    //一个权限可以授予多个角色，权限和角色之间属于一对多关系，彼此之间属于多对多关系
    private Set<Role>roles=new HashSet<>();
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

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }


}
