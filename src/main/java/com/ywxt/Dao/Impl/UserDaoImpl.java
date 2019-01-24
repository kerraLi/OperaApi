package com.ywxt.Dao.Impl;


import com.ywxt.Dao.UserDao;
import com.ywxt.Domain.Permission;
import com.ywxt.Domain.User;
import com.ywxt.Utils.PageBean;


import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Repository;


import java.io.Serializable;
import java.util.List;
@Repository
public class UserDaoImpl extends BaseDaoImpl<User> implements UserDao {





    public Long saveUser(User user) {
            return null;
    }

    public User getUserById(long id) {
            return null;
    }

    public User getUserByUsername(String username) throws Exception {
                return null;
    }


    public List<User> list() {
       String hql="FROM User";
        List<User> list = (List<User>) this.getHibernateTemplate().find(hql);
        return list;

    }

    //根据用户名查询用户
    public User findUserByUsername(String username) {
        String hql = "FROM User WHERE username = ?";
        List<User> list = (List<User>) this.getHibernateTemplate().find(hql, username);
        if(list != null && list.size() > 0){
            //查询到数据了
            return list.get(0);
        }
        return null;
    }

    @Override
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

}
