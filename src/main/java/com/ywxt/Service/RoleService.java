package com.ywxt.Service;


import com.ywxt.Domain.Permission;
import com.ywxt.Domain.Role;

import java.util.List;

public interface RoleService {

	public List<Role> findAll();

	public void delete(Role role);

	public void save(Role role);

	public Role findRoleById(Long id);

	public void update(Long role);

	public List<Role> findRoleByName(String name);


    void deleteById(Long id);

  /*  Role findByPermission(Permission permission);*/
}
