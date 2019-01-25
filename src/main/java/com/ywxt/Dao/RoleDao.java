package com.ywxt.Dao;


import com.ywxt.Domain.Role;
import com.ywxt.Utils.PageBean;
import org.hibernate.criterion.DetachedCriteria;


import java.io.Serializable;
import java.util.List;

public interface RoleDao /*extends IBaseDao<Role>*/{
	List<Role> findAll();
	void delete(Long id);
	Role findById(Long id);
	void update(Role role);
	List<Role> findRoleByName(String name);
}
