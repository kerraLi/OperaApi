package com.ywxt.Dao.Impl;

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

public class UserDaoImpl implements UserDao {

    private SessionFactory sessionFactory;

    public UserDaoImpl() {
        // 默认使用src文件夹下的hibernate.cfg.xml进行配置，若更改了路径，要附加上包路径如："/com/example/hibernate.cfg.xml"
        Configuration configuration = new Configuration();
        this.sessionFactory = configuration.configure().buildSessionFactory();
    }

    public Long saveUser(User user) {
        Transaction transaction = null;
        // try-with-resource 自动关闭资源
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Long id = (Long) session.save(user);
            transaction.commit();
            return id;
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public User getUserById(long id) {
        Transaction transaction = null;
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            // 主键查询，执行查询操作，1：get方法;2：load方法
            // 这种方式再try-with中实际使用时，调用数据库时，导致session已关闭
            //User u=(User)session.load(User.class, id);
            User u = (User) session.get(User.class, id);
            transaction.commit();
            return u;
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public User getUserByUsername(String username) throws Exception{
        Session session = this.sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> from = criteriaQuery.from(User.class);
        // 设置查询属性
        criteriaQuery.select(from).where(from.get("username").in(username));
        // 查询所有
        //criteriaQuery.from(User.class);
        List<User> list = session.createQuery(criteriaQuery).getResultList();
        //for (User user : list) {
        //    System.out.println(user.getUsername());
        //}
        if (list.size() == 0) {
            throw new Exception("账号名或密码错误");
        }
        return (User) session.createQuery(criteriaQuery).getResultList().get(0);
    }
}
