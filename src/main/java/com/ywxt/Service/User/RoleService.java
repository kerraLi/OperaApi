package com.ywxt.Service.User;

import com.ywxt.Domain.User.UserRole;

import java.util.List;

public interface RoleService {

    public List<UserRole> getList() throws Exception;

    public List<UserRole> getListValid() throws Exception;

    public Long save(UserRole userRole) throws Exception;

    public Long create(UserRole userRole) throws Exception;

    public Long update(UserRole userRole) throws Exception;

    public UserRole getRole(Long id) throws Exception;

    public Boolean remove(Long id) throws Exception;

    public Boolean remove(UserRole userRole) throws Exception;

}
