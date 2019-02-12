package com.ywxt.Service;


import com.ywxt.Domain.Permission;
import com.ywxt.Domain.Role;

import java.util.List;

public interface RoleService {

	public List<Role> findAll();


	public void save(Role role);

	public Role findRoleById(Long id);



	public List<Role> findRoleByName(String name);


    void deleteById(Long id);

	void update(Role role);

	/*  Role findByPermission(Permission permission);*/
}
