package com.ywxt.Service.User.Impl;

import com.ywxt.Dao.User.UserRoleDao;
import com.ywxt.Domain.User.UserRole;
import com.ywxt.Service.User.RoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Resource
    private UserRoleDao userRoleDao;

    @Override
    public List<UserRole> getList() throws Exception {
        return userRoleDao.getList(new HashMap<>() {{
        }});
    }

    @Override
    public List<UserRole> getListValid() throws Exception {
        List<UserRole> roles = userRoleDao.getListValid();
        // 单独查admin权限
        UserRole adminRole = userRoleDao.getUserRole("admin");
        roles.add(adminRole);
        return roles;
    }

    @Override
    public Long save(UserRole userRole) throws Exception {
        if (userRole.getId() == null) {
            return this.create(userRole);
        } else {
            return this.update(userRole);
        }
    }

    @Override
    public Long create(UserRole userRole) throws Exception {
        // todo code不能重复
        return userRoleDao.create(userRole);
    }

    @Override
    public Long update(UserRole userRole) throws Exception {
        // todo code是否重复 && admin不能修改
        userRoleDao.update(userRole);
        return userRole.getId();
    }

    @Override
    public UserRole getRole(Long id) throws Exception {
        return userRoleDao.getUserRole(id);
    }

    @Override
    public Boolean remove(Long id) throws Exception {
        return userRoleDao.delete(id);
    }

    @Override
    public Boolean remove(UserRole userRole) throws Exception {
        return userRoleDao.delete(userRole);
    }

}
