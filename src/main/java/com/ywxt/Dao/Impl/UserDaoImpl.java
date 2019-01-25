package com.ywxt.Dao.Impl;


import com.ywxt.Dao.CommonDao;
import com.ywxt.Dao.UserDao;
import com.ywxt.Domain.Permission;
import com.ywxt.Domain.User;
import com.ywxt.Utils.PageBean;


import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Repository;


import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;
@Repository
public class UserDaoImpl extends CommonDao /*BaseDaoImpl<User>*/ implements UserDao {





    public Long saveUser(User user) {
        try {
            session.beginTransaction();
            Long id = (Long) session.save(user);
            session.getTransaction().commit();
            return id;
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            this.closeSession();
        }
    }

    public User getUserById(long id) {
        try {
            session.beginTransaction();
            User u = (User) session.get(User.class, id);
            session.getTransaction().commit();
            return u;
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            this.closeSession();
        }
    }

    public User getUserByUsername(String username) throws Exception {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> from = criteriaQuery.from(User.class);
        // 设置查询属性
        criteriaQuery.select(from).where(from.get("username").in(username));
        List<User> list = session.createQuery(criteriaQuery).getResultList();
        if (list.size() == 0) {
            throw new Exception("账号名或密码错误");
        }
        User u = (User) list.get(0);
        this.closeSession();
        return u;
    }



    @Override
    public List<User> list() {
        return null;
    }

    @Override
    public Long add(String username, String password) {
        try {
            session.beginTransaction();
            User user = new User();
            user.setPassword(password);
            user.setUsername(username);
            Long id = (Long) session.save(user);
            session.getTransaction().commit();
            return id;
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            this.closeSession();
        }
    }

   /* public List<User> list() {
       String hql="FROM User";
        List<User> list = (List<User>) this.getHibernateTemplate().find(hql);
        return list;

    }*/

    /*//根据用户名查询用户
    public User findUserByUsername(String username) {
        String hql = "FROM User WHERE username = ?";
        List<User> list = (List<User>) this.getHibernateTemplate().find(hql, username);
        if(list != null && list.size() > 0){
            //查询到数据了
            return list.get(0);
        }
        return null;
    }*/
  /*  @Override
    public void save(User entity) {
    //        String hql="INSERT INTO User()VALUES()";
    }

    @Override
    public void delete(User entity) {

    }

    @Override
    public void update(User entity) {

    }

    @Override
    public void saveOrUpdate(User entity) {

    }

    @Override
    public User findById(Serializable id) {
        String hql = "FROM User WHERE id = ?";
        List<User> list = (List<User>) this.getHibernateTemplate().find(hql, id);
        if(list != null && list.size() > 0){
            //查询到数据了
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public void pageQuery(PageBean pageBean) {

    }

    @Override
    public void executeUpdate(String queryName, Object... objects) {

    }

    @Override
    public List<User> findByCriteria(DetachedCriteria detachedCriteria) {
        return null;
    }

    @Override
    public int add(Permission permission) {
        return 0;
    }
*/
}
