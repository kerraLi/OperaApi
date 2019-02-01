package com.ywxt.Domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

   /*            @JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })*/
   /*    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class,property = "id")*/
public class Role implements Serializable {

    private Long id;
    private String roleName;
    private String roleDesc;
    //一个角色可以授予多个用户，角色和用户之间属于一对多关系,彼此之间属于多对多关系

    private Set<User> users=new HashSet<>(0);
    //一个角色可以有多个权限，角色和权限之间属于一对多关系，彼此之间属于多对多关系
    private Set<Permission> permissions=new HashSet<>(0);

    public Role() {
    }

    public Role(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;
        Role role = (Role) o;
        return getId().equals(role.getId()) &&
                getRoleName().equals(role.getRoleName()) &&
                getRoleDesc().equals(role.getRoleDesc()) &&
                getUsers().equals(role.getUsers()) &&
                getPermissions().equals(role.getPermissions());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getRoleName(), getRoleDesc(), getUsers(), getPermissions());
    }

    public Role(String roleName, String roleDesc, Set<User> users, Set<Permission> permissions) {
        this.roleName = roleName;
        this.roleDesc = roleDesc;
        this.users = users;
        this.permissions = permissions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }
       @JsonIgnore
    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }


}
