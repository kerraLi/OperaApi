package com.ywxt.Dao.Impl;

import com.ywxt.Dao.CommonDao;
import com.ywxt.Dao.UserDao;
import com.ywxt.Domain.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class UserDaoImpl extends CommonDao implements UserDao {

    protected String domain = "User";

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
}
