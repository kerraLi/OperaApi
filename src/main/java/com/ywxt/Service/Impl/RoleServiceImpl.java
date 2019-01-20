package com.ywxt.Service.Impl;


import com.ywxt.Dao.RoleDao;
import com.ywxt.Domain.Role;
import com.ywxt.Service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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
	
	/**
	 * 查询岗位列表
	 */
	public List<Role> findAll() {
		return roleDao.findAll();
	}

	@Override
	/**
	 * 删除岗位实体
	 */
	public void delete(Role model) {
		roleDao.delete(model.getId());
	}

	/**
	 * 添加岗位
	 */
	public void save(Role model) {
		roleDao.save(model);
	}
	
	/**
	 * 根据id查询岗位
	 */
	public Role findRoleById(Long id) {
		return roleDao.findById(id);
	}

	/**
	 * 修改岗位
	 */
	public void update(Role role) {
		roleDao.update(role);
	}

	/**
	 * 根据名称查询岗位
	 */
	public List<Role> findRoleByName(String name) {
		return roleDao.findRoleByName(name);
	}

	/**
	 * 根据id数组查询多个岗位
	 */
	public List<Role> findRolesByIds(Long[] roleIds) {
		return roleDao.findByIds(roleIds);
	}
}
