package com.ywxt.Domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;
@Entity
public class Permission {
    @Id
    @GeneratedValue
    private Long id;
    private String permissionName;
    private String url;
    private Long pid;
    //一个权限可以授予多个角色，权限和角色之间属于一对多关系，彼此之间属于多对多关系
    private List<Role> rols;

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

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public List<Role> getRols() {
        return rols;
    }

    public void setRols(List<Role> rols) {
        this.rols = rols;
    }
}
