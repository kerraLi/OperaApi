package com.ywxt.Dao.Impl;


import com.ywxt.Dao.CommonDao;
import com.ywxt.Dao.RoleDao;

import com.ywxt.Domain.Role;
import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;
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

		try {
			Role role = session.get(Role.class, id);
			return role;
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public void update(Long id) {
		try {

			session.beginTransaction();
			Role role = session.get(Role.class, id);
			session.update(role);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}finally {
			session.close();
		}
	}
// 模糊查询 角色名字
	@Override
	public List<Role> findRoleByName(String name) {

		try {
			Criteria criteria = session.createCriteria(Role.class);
			criteria.add(Expression.like("roleName","%"+name+"%"));
			List<Role> list = criteria.list();
			return list;
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public void save(Role role) {
		try {
		//	model.setRoleName("会计");
	        session.beginTransaction();
			session.save(role);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}finally {
              session.close();
		}
	}
}
