package com.ywxt.Dao.Impl;


import com.ywxt.Dao.CommonDao;
import com.ywxt.Dao.UserDao;
import com.ywxt.Domain.User;


import org.hibernate.Criteria;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;


import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
@Repository
public class UserDaoImpl extends CommonDao implements UserDao {





    public Long saveUser(User user) {
        Transaction transaction=null;
        try {
           transaction = session.beginTransaction();
            Long id = (Long) session.save(user);
          transaction.commit();
            return id;
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public User getUserById(long id) {
        User u =  session.get(User.class, id);
            session.close();
            return u;
    }
            //根据用户名查询用户
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
        Criteria criteria = session.createCriteria(User.class);
        List<User> list = criteria.list();
        return list;
    }

    @Override
    public Long add(String username, String password) {
        Transaction transaction=null;
        try {
            transaction = session.beginTransaction();
            User user = new User();
            user.setPassword(password);
            user.setUsername(username);

            Long id = (Long) session.save(user);
            transaction.commit();
            return id;
        } catch (Exception e) {
          transaction.rollback();
            throw e;
        } finally {
         session.close();
        }
    }

    @Override
    public void updateUserById(User user) {
        Transaction transaction=null;
        try {
            transaction = session.beginTransaction();
            session.update(user);
        transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public void deleteById(long id) {
        Transaction transaction=null;
        try {
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            session.delete(user);
           transaction.commit();
        } catch (Exception e) {
           transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }
}
