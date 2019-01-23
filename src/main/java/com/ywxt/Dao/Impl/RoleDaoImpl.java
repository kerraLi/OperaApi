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
 * @author
 *
 */
@Repository
public class RoleDaoImpl extends BaseDaoImpl<Role> implements RoleDao {


	@Override
	public void delete(Long id) {

	}

	@Override
	public Role findById(Long id) {
		return null;
	}

	@Override
	public List<Role> findRoleByName(String name) {
		return null;
	}
}
