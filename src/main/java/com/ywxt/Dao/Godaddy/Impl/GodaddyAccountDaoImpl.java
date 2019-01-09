package com.ywxt.Dao.Godaddy.Impl;

import com.ywxt.Dao.Godaddy.GodaddyAccountDao;
import com.ywxt.Domain.Godaddy.GodaddyAccount;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class GodaddyAccountDaoImpl implements GodaddyAccountDao {

    private SessionFactory sessionFactory;

    public GodaddyAccountDaoImpl() {
        Configuration configuration = new Configuration();
        this.sessionFactory = configuration.configure().buildSessionFactory();
    }

    // 根据id查找账号
    public GodaddyAccount getAccount(int id) {
        Session session = this.sessionFactory.openSession();
        GodaddyAccount godaddyAccount = (GodaddyAccount) session.get(GodaddyAccount.class, id);
        return godaddyAccount;
    }

    // 根据accessKeyId查找账号
    public GodaddyAccount getAccount(String accessKeyId) {
        Session session = this.sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<GodaddyAccount> criteriaQuery = criteriaBuilder.createQuery(GodaddyAccount.class);
        Root<GodaddyAccount> from = criteriaQuery.from(GodaddyAccount.class);
        // 设置查询属性
        criteriaQuery.select(from).where(from.get("accessKeyId").in(accessKeyId));
        return (GodaddyAccount) session.createQuery(criteriaQuery).getResultList().get(0);
    }

    // 获取正常账号
    public List<GodaddyAccount> getAccountsNormal() {
        Session session = this.sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<GodaddyAccount> criteriaQuery = criteriaBuilder.createQuery(GodaddyAccount.class);
        Root<GodaddyAccount> root = criteriaQuery.from(GodaddyAccount.class);
        // 查询条件
        Predicate predicate = criteriaBuilder.equal(root.get("status"), "normal");
        criteriaQuery.where(predicate);

        return session.createQuery(criteriaQuery).getResultList();
    }

    // 账号列表
    public List<GodaddyAccount> getAccounts() {
        Session session = this.sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<GodaddyAccount> criteriaQuery = criteriaBuilder.createQuery(GodaddyAccount.class);
        criteriaQuery.from(GodaddyAccount.class);
        return session.createQuery(criteriaQuery).getResultList();
    }

    // 保存更新
    public int saveAccount(GodaddyAccount account) {
        Transaction transaction = null;
        // try-with-resource 自动关闭资源
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            int id = (int) session.save(account);
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
    public boolean deleteAccount(int accountId) {
        Transaction transaction = null;
        // try-with-resource 自动关闭资源
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            GodaddyAccount godaddyAccount = (GodaddyAccount) session.get(GodaddyAccount.class, accountId);
            session.delete(godaddyAccount);
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
