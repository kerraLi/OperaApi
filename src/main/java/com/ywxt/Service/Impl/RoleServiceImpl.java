package com.ywxt.Service.Impl;


import com.ywxt.Dao.RoleDao;
import com.ywxt.Domain.Role;
import com.ywxt.Service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 岗位管理Service
 * @author
 *
 */
@Service
@Transactional
public class RoleServiceImpl implements RoleService {
	@Autowired
	private RoleDao roleDao;

	@Override
	public List<Role> findAll() {
		return null;
	}

	@Override
	public void delete(Role role) {

	}

	@Override
	public void save(Role role) {
			roleDao.save(role);
	}

	@Override
	public Role findRoleById(Long id) {
		return null;
	}

	@Override
	public void update(Long id) {
		roleDao.update(id);
	}

	@Override
	public List<Role> findRoleByName(String name) {
		return null;
	}

	@Override
	public void deleteById(Long id) {
			roleDao.delete(id);
	}


}
