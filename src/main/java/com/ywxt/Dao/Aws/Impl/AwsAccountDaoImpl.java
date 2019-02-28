package com.ywxt.Dao.Aws.Impl;

import com.ywxt.Dao.CommonDao;
import com.ywxt.Domain.Aws.AwsAccount;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.HashMap;
import java.util.List;

public class AwsAccountDaoImpl extends CommonDao {
    protected String domain = "AwsAccount";

    // 个数
    public int getListTotal(HashMap<String, Object> params) throws Exception {
        Criteria criteria = this.getCriteria(AwsAccount.class, params);
        criteria.setProjection(Projections.rowCount());
        int total = Integer.parseInt(criteria.uniqueResult().toString());
        this.closeSession();
        return total;
    }

    // 根据id查找账号
    public AwsAccount getAccount(int id) {
        AwsAccount awsAccount = (AwsAccount) session.get(AwsAccount.class, id);
        this.closeSession();
        return awsAccount;
    }

    // 根据accessKeyId查找账号
    public AwsAccount getAccount(String accessKeyId) throws Exception {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<AwsAccount> criteriaQuery = criteriaBuilder.createQuery(AwsAccount.class);
        Root<AwsAccount> from = criteriaQuery.from(AwsAccount.class);
        // 设置查询属性
        criteriaQuery.select(from).where(from.get("accessKeyId").in(accessKeyId));
        List<AwsAccount> list = session.createQuery(criteriaQuery).getResultList();
        if (list.size() == 0) {
            throw new Exception("无该账号");
        }
        AwsAccount awsAccount = (AwsAccount) list.get(0);
        this.closeSession();
        return awsAccount;
    }

    // 获取正常账号
    public List<AwsAccount> getAccountsNormal() {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<AwsAccount> criteriaQuery = criteriaBuilder.createQuery(AwsAccount.class);
        Root<AwsAccount> root = criteriaQuery.from(AwsAccount.class);
        // 查询条件
        Predicate predicate = criteriaBuilder.equal(root.get("status"), "normal");
        criteriaQuery.where(predicate);
        List<AwsAccount> list = session.createQuery(criteriaQuery).getResultList();
        this.closeSession();
        return list;
    }

    // 账号列表
    public List<AwsAccount> getAccounts() {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<AwsAccount> criteriaQuery = criteriaBuilder.createQuery(AwsAccount.class);
        criteriaQuery.from(AwsAccount.class);
        List<AwsAccount> list = session.createQuery(criteriaQuery).getResultList();
        this.closeSession();
        return list;
    }

    // 保存更新
    public int saveAccount(AwsAccount account) {
        int id;
        if (account.getId() != 0) {
            id = account.getId();
            try {
                session.beginTransaction();
                session.update(account);
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
            } finally {
                this.closeSession();
            }
        } else {
            id = (int) session.save(account);
            this.closeSession();
        }
        return id;
    }

    // 删除
    public boolean deleteAccount(int accountId) {
        AwsAccount awsAccount = (AwsAccount) session.get(AwsAccount.class, accountId);
        try {
            session.beginTransaction();
            session.delete(awsAccount);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            this.closeSession();
        }
        return true;
    }
}
