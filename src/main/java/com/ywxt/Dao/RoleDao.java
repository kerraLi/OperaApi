package com.ywxt.Dao;


import com.ywxt.Domain.Role;


import java.util.List;

public interface RoleDao{
	List<Role> findAll();
	void delete(Long id);
	Role findById(Long id);
	void update(Long role);
	List<Role> findRoleByName(String name);

	void save(Role role);
}
