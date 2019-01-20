package com.ywxt.Dao.Impl;


import com.ywxt.Dao.CommonDao;
import com.ywxt.Dao.RoleDao;

import com.ywxt.Domain.Role;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 岗位管理Dao
 * @author zhaoqx
 *
 */
@Repository
public class RoleDaoImpl extends CommonDao implements RoleDao {
	@Override
	public List<Role> findAll() {
		return null;
	}

	@Override
	public void delete(Long id) {

	}

	@Override
	public Role findById(Long id) {
		return null;
	}

	@Override
	public void save(Role model) {

	}

	@Override
	public void update(Role role) {

	}

	@Override
	public List<Role> findRoleByName(String name) {
		return null;
	}

	@Override
	public List<Role> findByIds(Long[] roleIds) {
		return null;
	}
	/**
	 * 根据名称查询岗位
	 */


}
