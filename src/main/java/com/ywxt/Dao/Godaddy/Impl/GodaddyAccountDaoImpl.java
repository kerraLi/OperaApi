package com.ywxt.Dao.Godaddy.Impl;

import com.ywxt.Dao.CommonDao;
import com.ywxt.Dao.Godaddy.GodaddyAccountDao;
import com.ywxt.Domain.Godaddy.GodaddyAccount;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.HashMap;
import java.util.List;

public class GodaddyAccountDaoImpl extends CommonDao implements GodaddyAccountDao {

    protected String domain = "GodaddyAccount";

    // 个数
    public int getListTotal(HashMap<String, Object> params) throws Exception {
        Criteria criteria = this.getCriteria(GodaddyAccount.class, params);
        criteria.setProjection(Projections.rowCount());
        int total = Integer.parseInt(criteria.uniqueResult().toString());
        this.closeSession();
        return total;
    }

    // 根据id查找账号
    public GodaddyAccount getAccount(int id) {
        GodaddyAccount godaddyAccount = (GodaddyAccount) session.get(GodaddyAccount.class, id);
        this.closeSession();
        return godaddyAccount;
    }

    // 根据accessKeyId查找账号
    public GodaddyAccount getAccount(String accessKeyId) throws Exception {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<GodaddyAccount> criteriaQuery = criteriaBuilder.createQuery(GodaddyAccount.class);
        Root<GodaddyAccount> from = criteriaQuery.from(GodaddyAccount.class);
        // 设置查询属性
        criteriaQuery.select(from).where(from.get("accessKeyId").in(accessKeyId));
        List<GodaddyAccount> list = session.createQuery(criteriaQuery).getResultList();
        if (list.size() == 0) {
            throw new Exception("无该账号");
        }
        GodaddyAccount godaddyAccount = (GodaddyAccount) list.get(0);
        this.closeSession();
        return godaddyAccount;
    }

    // 获取正常账号
    public List<GodaddyAccount> getAccountsNormal() {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<GodaddyAccount> criteriaQuery = criteriaBuilder.createQuery(GodaddyAccount.class);
        Root<GodaddyAccount> root = criteriaQuery.from(GodaddyAccount.class);
        // 查询条件
        Predicate predicate = criteriaBuilder.equal(root.get("status"), "normal");
        criteriaQuery.where(predicate);
        List<GodaddyAccount> list = session.createQuery(criteriaQuery).getResultList();
        this.closeSession();
        return list;
    }

    // 账号列表
    public List<GodaddyAccount> getAccounts() {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<GodaddyAccount> criteriaQuery = criteriaBuilder.createQuery(GodaddyAccount.class);
        criteriaQuery.from(GodaddyAccount.class);
        List<GodaddyAccount> list = session.createQuery(criteriaQuery).getResultList();
        this.closeSession();
        return list;
    }

    // 保存更新
    public int saveAccount(GodaddyAccount account) {
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
        GodaddyAccount godaddyAccount = (GodaddyAccount) session.get(GodaddyAccount.class, accountId);
        try {
            session.beginTransaction();
            session.delete(godaddyAccount);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            this.closeSession();
        }
        return true;
    }
}
