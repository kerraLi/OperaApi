package com.ywxt.Dao;


import com.ywxt.Domain.Role;


import java.util.List;

public interface RoleDao {


	List<Role> findAll();

	void delete(Long id);

	Role findById(Long id);

	Long save(Role role);

	void update(Role role);

	List<Role> findRoleByName(String name);

	List<Role> findByIds(Long[] roleIds);
}
