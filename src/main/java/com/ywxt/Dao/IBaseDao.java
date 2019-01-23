package com.ywxt.Dao;


import com.ywxt.Domain.Permission;
import com.ywxt.Utils.PageBean;
import org.hibernate.criterion.DetachedCriteria;

import java.io.Serializable;
import java.util.List;


/**
 * 抽取持久层通用方法
 * @author zhaoqx
 *
 * @param <T>
 */
public interface IBaseDao<T> {
    public void save(T entity);
	public void delete(T entity);
	public void update(T entity);
	public void saveOrUpdate(T entity);
	public T findById(Serializable id);
	public List<T> findAll();
	public void pageQuery(PageBean pageBean);
	public void executeUpdate(String queryName, Object... objects);
	public List<T> findByCriteria(DetachedCriteria detachedCriteria);

	int add(Permission permission);
}
