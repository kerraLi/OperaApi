package com.ywxt.Dao.Impl;


import com.ywxt.Dao.CommonDao;
import com.ywxt.Dao.RoleDao;

import com.ywxt.Domain.Role;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 岗位管理Dao
 * @author zhaoqx
 *
 */
@Repository
public class RoleDaoImpl extends CommonDao implements RoleDao {
//	protected String domain = "Role";
	@Override
	public List<Role> findAll() {
		try {
			Criteria criteria = session.createCriteria(Role.class);
			List<Role>roles=criteria.list();
			return roles;
		} catch (HibernateException e) {
			session.getTransaction().rollback();
			throw e;
		} finally {
			this.closeSession();
		}
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

		session.getTransaction().commit();
		return 1l;
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
