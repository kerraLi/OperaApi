package com.ywxt.Service.User;

import com.ywxt.Domain.User.UserRole;

import java.util.List;

public interface RoleService {

    List<UserRole> getList() throws Exception;

    List<UserRole> getListValid() throws Exception;

    Long save(UserRole userRole) throws Exception;

    Long create(UserRole userRole) throws Exception;

    Long update(UserRole userRole) throws Exception;

    UserRole getRole(Long id) throws Exception;

    Boolean remove(Long id) throws Exception;

    Boolean remove(UserRole userRole) throws Exception;

}
