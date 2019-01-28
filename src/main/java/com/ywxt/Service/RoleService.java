package com.ywxt.Service;


import com.ywxt.Domain.Role;

import java.util.List;

public interface RoleService {

	public List<Role> findAll();

	public void delete(Role model);

	public void save(Role model);

	public Role findRoleById(Long id);

	public void update(Role role);

	public List<Role> findRoleByName(String name);


    void deleteById(Long id);
}
