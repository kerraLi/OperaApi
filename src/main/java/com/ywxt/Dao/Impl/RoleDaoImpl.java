package com.ywxt.Dao.Impl;


import com.ywxt.Dao.CommonDao;
import com.ywxt.Dao.RoleDao;

import com.ywxt.Domain.Role;
import org.hibernate.Criteria;
import org.hibernate.Transaction;
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
		List<Role> list = session.createCriteria(Role.class).list();
		return list;
	}

	@Override
	public void delete(Long id) {
		Transaction transaction=null;
		try {
			transaction = session.beginTransaction();
			Role role = new Role();
			role.setId(id);
			session.delete(role);
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			transaction.rollback();
		}finally {
			session.close();
		}
	}

	@Override
	public Role findById(Long id) {
		Role role = session.get(Role.class, id);
		session.close();
		return role;
	}

	@Override
	public void update(Role role) {
		Transaction transaction=null;
		try {
			transaction = session.beginTransaction();
			session.update(role);
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			transaction.rollback();
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
		Transaction transaction=null;
		try {
			transaction = session.beginTransaction();
			session.save(role);
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			transaction.rollback();
		}finally {
              session.close();
		}
	}

	/*@Override
	public Role findByPermission(Permission permission) {
		Criteria criteria = session.createCriteria(Role.class);
		criteria.add(Expression.eq(""))
		return null;
	}*/
}
