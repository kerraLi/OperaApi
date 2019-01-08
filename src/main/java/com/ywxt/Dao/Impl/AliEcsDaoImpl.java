package com.ywxt.Dao.Impl;

import com.ywxt.Dao.AliEcsDao;
import com.ywxt.Domain.AliEcs;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Order;

import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AliEcsDaoImpl implements AliEcsDao {

    private SessionFactory sessionFactory;
    private String domain = "AliEcs";

    public AliEcsDaoImpl() {
        Configuration configuration = new Configuration();
        this.sessionFactory = configuration.configure().buildSessionFactory();
    }

    // 获取数量
    public int getAliEcsesTotal(HashMap<String, Object> params) {
        Session session = this.sessionFactory.openSession();
        Criteria criteria = session.createCriteria(AliEcs.class);
        if (params != null) {
            for (Map.Entry<String, Object> e : params.entrySet()) {
                if (e.getKey().equals("orderAsc")) {
                    criteria.addOrder(Order.asc((String) e.getValue()));
                } else if (e.getKey().equals("orderDesc")) {
                    criteria.addOrder(Order.desc((String) e.getValue()));
                } else {
                    criteria.add(Restrictions.eq(e.getKey(), e.getValue()));
                }
            }
        }
        criteria.addOrder(Order.asc("id"));
        criteria.setProjection(Projections.rowCount());

        return Integer.parseInt(criteria.uniqueResult().toString());
    }

    // 查找所有
    public List<AliEcs> getAliEcsesList(HashMap<String, Object> params) {
        Session session = this.sessionFactory.openSession();
        Criteria criteria = session.createCriteria(AliEcs.class);
        if (params != null) {
            for (Map.Entry<String, Object> e : params.entrySet()) {
                if (e.getKey().equals("orderAsc")) {
                    criteria.addOrder(Order.asc((String) e.getValue()));
                } else if (e.getKey().equals("orderDesc")) {
                    criteria.addOrder(Order.desc((String) e.getValue()));
                } else {
                    criteria.add(Restrictions.eq(e.getKey(), e.getValue()));
                }
            }
        }
        criteria.addOrder(Order.asc("id"));
        List<AliEcs> list = criteria.list();
        session.close();
        return list;
    }

    // 分页查找
    public List<AliEcs> getAliEcsesList(HashMap<String, Object> params, int pageNumber, int pageSize) {
        Session session = this.sessionFactory.openSession();
        Criteria criteria = session.createCriteria(AliEcs.class);
        if (params != null) {
            for (Map.Entry<String, Object> e : params.entrySet()) {
                if (e.getKey().equals("orderAsc")) {
                    criteria.addOrder(Order.asc((String) e.getValue()));
                } else if (e.getKey().equals("orderDesc")) {
                    criteria.addOrder(Order.desc((String) e.getValue()));
                } else {
                    criteria.add(Restrictions.eq(e.getKey(), e.getValue()));
                }
            }
        }
        criteria.addOrder(Order.asc("id"));
        criteria.setFirstResult((pageNumber - 1) * pageSize);
        criteria.setMaxResults(pageSize);
        List<AliEcs> list = criteria.list();
        session.close();
        return list;
    }

    // 批量保存
    public void saveAliEcses(List<AliEcs> list) {
        Session session = this.sessionFactory.openSession();
        for (AliEcs aliEcs : list) {
            session.save(aliEcs);
        }
        session.close();
    }

    // 保存更新
    public int saveAliEcs(AliEcs aliEcs) {
        Session session = this.sessionFactory.openSession();
        int id = (int) session.save(aliEcs);
        session.close();
        return id;
    }

    // 删除
    public void deleteAliEcsByAccessId(String accessId) {
        Session session = this.sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        String hqlDelete = "delete " + this.domain + " where accessKeyId = :accessKeyId";
        session.createQuery(hqlDelete)
                .setParameter("accessKeyId", accessId)
                .executeUpdate();
        tx.commit();
        session.close();
    }
}
