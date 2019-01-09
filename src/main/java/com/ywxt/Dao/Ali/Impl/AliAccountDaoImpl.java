package com.ywxt.Dao.Ali.Impl;

import com.ywxt.Dao.Ali.AliAccountDao;
import com.ywxt.Domain.Ali.AliAccount;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import javax.persistence.criteria.*;
import java.util.List;

public class AliAccountDaoImpl implements AliAccountDao {


    private SessionFactory sessionFactory;

    public AliAccountDaoImpl() {
        Configuration configuration = new Configuration();
        this.sessionFactory = configuration.configure().buildSessionFactory();
    }

    // 根据id查找账号
    public AliAccount getAliAccount(int id) {
        Session session = this.sessionFactory.openSession();
        AliAccount aliAccount = (AliAccount) session.get(AliAccount.class, id);
        return aliAccount;
    }

    // 根据accessKeyId查找账号
    public AliAccount getAliAccount(String accessKeyId) {
        Session session = this.sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<AliAccount> criteriaQuery = criteriaBuilder.createQuery(AliAccount.class);
        Root<AliAccount> from = criteriaQuery.from(AliAccount.class);
        // 设置查询属性
        criteriaQuery.select(from).where(from.get("accessKeyId").in(accessKeyId));
        return (AliAccount) session.createQuery(criteriaQuery).getResultList().get(0);
    }

    // 获取正常账号
    public List<AliAccount> getAliAccountsNormal() {
        Session session = this.sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<AliAccount> criteriaQuery = criteriaBuilder.createQuery(AliAccount.class);
        Root<AliAccount> root = criteriaQuery.from(AliAccount.class);
        // 查询条件
        Predicate predicate = criteriaBuilder.equal(root.get("status"), "normal");
        criteriaQuery.where(predicate);

        return session.createQuery(criteriaQuery).getResultList();
    }


    // 账号列表
    public List<AliAccount> getAliAccounts() {
        Session session = this.sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<AliAccount> criteriaQuery = criteriaBuilder.createQuery(AliAccount.class);
        criteriaQuery.from(AliAccount.class);
        return session.createQuery(criteriaQuery).getResultList();
    }

    // 保存更新
    public int saveAliAccount(AliAccount aliAccount) {
        Transaction transaction = null;
        // try-with-resource 自动关闭资源
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            int id = (int) session.save(aliAccount);
            transaction.commit();
            return id;
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    // 删除
    public boolean deleteAliAccount(int aliAccountId) {
        Transaction transaction = null;
        // try-with-resource 自动关闭资源
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            AliAccount aliAccount = (AliAccount) session.get(AliAccount.class, aliAccountId);
            session.delete(aliAccount);
            transaction.commit();
            return true;
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

}
