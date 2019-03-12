package com.ywxt.Dao.User;


import com.ywxt.Domain.User.UserRole;

import java.util.HashMap;
import java.util.List;

public interface UserRoleDao {

    public Long create(UserRole userRole);

    public boolean delete(long id);

    public boolean delete(String type);

    public boolean delete(UserRole userRole);

    public UserRole update(UserRole userRole);

    public UserRole getUserRole(long id);

    public UserRole getUserRole(String code) throws Exception;

    public List<UserRole> getListValid();

    public List<UserRole> getList(HashMap<String, Object> params);

    public List<UserRole> getList(HashMap<String, Object> params, int pageNumber, int pageSize);

    public int getListTotal(HashMap<String, Object> params);
}
