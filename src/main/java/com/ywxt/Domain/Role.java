package com.ywxt.Domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@Entity
public class Role implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private String roleName;
    private String roleDesc;
    //一个角色可以授予多个用户，角色和用户之间属于一对多关系,彼此之间属于多对多关系
    private Set<User> users=new HashSet<>();
    //一个角色可以有多个权限，角色和权限之间属于一对多关系，彼此之间属于多对多关系
    private Set<Permission> permissions=new HashSet<>();

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
