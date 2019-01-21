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
	protected String domain = "Role";
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
	public Long save(Role role) {
          session.beginTransaction();
		Long id = (Long) session.save(role);
		session.getTransaction().commit();
		return id;
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
