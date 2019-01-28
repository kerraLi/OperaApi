package com.ywxt.Dao.Impl;


import com.ywxt.Dao.CommonDao;
import com.ywxt.Dao.RoleDao;

import com.ywxt.Domain.Role;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 岗位管理Dao
 * @author
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
		try {

			session.beginTransaction();
			Role role=session.get(Role.class,id);

			session.delete(role);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}finally {
			session.close();
		}
	}

	@Override
	public Role findById(Long id) {
		return null;
	}

	@Override
	public void update(Role role) {
		try {

			session.beginTransaction();
			session.update(role);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}finally {
			session.close();
		}
	}

	@Override
	public List<Role> findRoleByName(String name) {
		return null;
	}

	@Override
	public void save(Role model) {
		try {
		//	model.setRoleName("会计");
	        session.beginTransaction();
			session.save(model);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}finally {
              session.close();
		}
	}
}
