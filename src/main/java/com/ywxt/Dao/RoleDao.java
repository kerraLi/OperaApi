package com.ywxt.Dao;


import com.ywxt.Domain.Role;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface RoleDao {


	List<Role> findAll();

	void delete(Long id);

	Role findById(Long id);

	void save(Role model);

	void update(Role role);

	List<Role> findRoleByName(String name);

	List<Role> findByIds(Long[] roleIds);
}
