package com.ywxt.Dao.Impl;

import com.ywxt.Dao.AliAccountDao;
import com.ywxt.Domain.AliAccount;
import com.ywxt.Domain.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class AliAccountDaoImpl implements AliAccountDao {


    private SessionFactory sessionFactory;

    public AliAccountDaoImpl() {
        Configuration configuration = new Configuration();
        this.sessionFactory = configuration.configure().buildSessionFactory();
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
