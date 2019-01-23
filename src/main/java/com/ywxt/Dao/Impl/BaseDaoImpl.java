package com.ywxt.Dao.Impl;


import com.ywxt.Dao.IBaseDao;
import com.ywxt.Domain.Permission;
import com.ywxt.Utils.PageBean;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import javax.annotation.Resource;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 持久层通用实现
 * @author zhaoqx
 *
 * @param <T>
 */
public class BaseDaoImpl<T> extends HibernateDaoSupport implements IBaseDao<T> {
	private Class<T> entityClass;//代表操作的实体的类型
	
	@Resource//可以按照bean的id注入，也可以按照bean的类型注入
	//@Autowired//只能按照类型注入,如果需要按照id注入，需要结合@Qualifier注解一起使用
	//@Qualifier(value="abc")
	public void setMySessionFactory(SessionFactory sessionFactory){
		super.setSessionFactory(sessionFactory);
	}
	
	//在构造方法中动态获取当前操作的实体类型
	public BaseDaoImpl() {
		//获得父类（BaseDaoImpl<T>）的类型
		ParameterizedType genericSuperclass = (ParameterizedType) this.getClass().getGenericSuperclass();
		//获得父类上声明的泛型数组
		Type[] actualTypeArguments = genericSuperclass.getActualTypeArguments();
		entityClass = (Class) actualTypeArguments[0];
	}
	
	public void save(T entity) {
		this.getHibernateTemplate().save(entity);
	}

	public void delete(T entity) {
		this.getHibernateTemplate().delete(entity);
	}

	public void update(T entity) {
		this.getHibernateTemplate().update(entity);
	}

	public T findById(Serializable id) {
		return (T) this.getHibernateTemplate().get(entityClass, id);
	}

	public List<T> findAll() {
		String hql = "FROM  " + entityClass.getSimpleName();
		return (List<T>) this.getHibernateTemplate().find(hql);
	}



	//实现持久层通用分页查询
	public void pageQuery(PageBean pageBean) {
		int currentPage = pageBean.getCurrentPage();
		int pageSize = pageBean.getPageSize();
		DetachedCriteria detachedCriteria = pageBean.getDetachedCriteria();
		//改变hibernate发出的sql的形式，改为：select count(*) from ....
		detachedCriteria.setProjection(Projections.rowCount());
		//查询数据库，获取total---总数据量
		List<Long> totalList = (List<Long>) this.getHibernateTemplate().findByCriteria(detachedCriteria);
		Long total = totalList.get(0);
		pageBean.setTotal(total.intValue());
		//改变hibernate发出的sql的形式，改为：select * from ....
		detachedCriteria.setProjection(null);
		
		//指定hibernate框架如何封装对象：在多表关联查询时，发送查询多张表的sql语句，依然封装成指定的ROOT对象
		detachedCriteria.setResultTransformer(DetachedCriteria.ROOT_ENTITY);
		
		int firstResult = (currentPage - 1) * pageSize;//从哪开始查询
		int maxResults = pageSize;//查询几条
		//查询数据库，获取rows---当前页要展示的数据集合
		List rows = this.getHibernateTemplate().findByCriteria(detachedCriteria, firstResult , maxResults );
		pageBean.setRows(rows);
	}

	//通用的更新方法
	public void executeUpdate(String queryName, Object... objects) {
		Query query = this.getSessionFactory().getCurrentSession().getNamedQuery(queryName);
		int i = 0;
		for (Object object : objects) {
			//每次循环为？赋值
			query.setParameter(i++,object);
		}
		//执行更新
		query.executeUpdate();
	}

	public void saveOrUpdate(T entity) {
		this.getHibernateTemplate().saveOrUpdate(entity);
	}

	//根据离线条件查询对象进行查询
	public List<T> findByCriteria(DetachedCriteria detachedCriteria) {
		return (List<T>) this.getHibernateTemplate().findByCriteria(detachedCriteria);
	}

	@Override
	public int add(Permission permission) {
		return 0;
	}
}
