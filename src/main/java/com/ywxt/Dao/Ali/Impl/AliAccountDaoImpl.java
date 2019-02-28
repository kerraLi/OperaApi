package com.ywxt.Dao.Ali.Impl;

import com.ywxt.Dao.Ali.AliAccountDao;
import com.ywxt.Dao.CommonDao;
import com.ywxt.Domain.Ali.AliAccount;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;

import javax.persistence.criteria.*;
import java.util.HashMap;
import java.util.List;

public class AliAccountDaoImpl extends CommonDao implements AliAccountDao {

    protected String domain = "AliAccount";

    // 个数
    public int getListTotal(HashMap<String, Object> params) throws Exception {
        Criteria criteria = this.getCriteria(AliAccount.class, params);
        criteria.setProjection(Projections.rowCount());
        int total = Integer.parseInt(criteria.uniqueResult().toString());
        this.closeSession();
        return total;
    }

    // 根据id查找账号
    public AliAccount getAliAccount(int id) {
        AliAccount aliAccount = (AliAccount) session.get(AliAccount.class, id);
        this.closeSession();
        return aliAccount;
    }

    // 根据accessKeyId查找账号
    public AliAccount getAliAccount(String accessKeyId) throws Exception {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<AliAccount> criteriaQuery = criteriaBuilder.createQuery(AliAccount.class);
        Root<AliAccount> from = criteriaQuery.from(AliAccount.class);
        // 设置查询属性
        criteriaQuery.select(from).where(from.get("accessKeyId").in(accessKeyId));
        List<AliAccount> list = session.createQuery(criteriaQuery).getResultList();
        if (list.size() == 0) {
            throw new Exception("无该账号");
        }
        AliAccount aliAccount = (AliAccount) list.get(0);
        this.closeSession();
        return aliAccount;
    }

    // 获取正常账号
    public List<AliAccount> getAliAccountsNormal() {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<AliAccount> criteriaQuery = criteriaBuilder.createQuery(AliAccount.class);
        Root<AliAccount> root = criteriaQuery.from(AliAccount.class);
        // 查询条件
        Predicate predicate = criteriaBuilder.equal(root.get("status"), "normal");
        criteriaQuery.where(predicate);
        List<AliAccount> list = session.createQuery(criteriaQuery).getResultList();
        this.closeSession();
        return list;
    }


    // 账号列表
    public List<AliAccount> getAliAccounts() {
        Session session = this.sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<AliAccount> criteriaQuery = criteriaBuilder.createQuery(AliAccount.class);
        criteriaQuery.from(AliAccount.class);
        List<AliAccount> list = session.createQuery(criteriaQuery).getResultList();
        this.closeSession();
        return list;
    }

    // 保存更新
    public int saveAliAccount(AliAccount aliAccount) {
        int id;
        if (aliAccount.getId() != 0) {
            id = aliAccount.getId();
            try {
                session.beginTransaction();
                session.update(aliAccount);
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
            } finally {
                this.closeSession();
            }
        } else {
            id = (int) session.save(aliAccount);
            this.closeSession();
        }
        return id;
    }

    // 删除
    public boolean deleteAliAccount(int aliAccountId) {
        AliAccount aliAccount = (AliAccount) session.get(AliAccount.class, aliAccountId);
        try {
            session.beginTransaction();
            session.delete(aliAccount);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            this.closeSession();
        }
        return true;
    }

}
