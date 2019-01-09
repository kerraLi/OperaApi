package com.ywxt.Dao.Ali.Impl;

import com.ywxt.Dao.Ali.AliEcsDao;
import com.ywxt.Domain.AliEcs;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Disjunction;
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
                    String[] strings = e.getKey().split("@");
                    if (strings.length == 1) {
                        if (strings[0].equals("filter")) {
                            String filter = "%" + e.getValue() + "%";
                            // 多个or条件
                            Disjunction dis = Restrictions.disjunction();
                            dis.add(Restrictions.like("id", filter));
                            dis.add(Restrictions.like("accessKeyId", filter));
                            dis.add(Restrictions.like("instanceId", filter));
                            dis.add(Restrictions.like("instanceName", filter));
                            dis.add(Restrictions.like("instanceType", filter));
                            dis.add(Restrictions.like("instanceNetworkType", filter));
                            dis.add(Restrictions.like("hostName", filter));
                            dis.add(Restrictions.like("imageId", filter));
                            dis.add(Restrictions.like("regionId", filter));
                            dis.add(Restrictions.like("zoneId", filter));
                            dis.add(Restrictions.like("creationTime", filter));
                            dis.add(Restrictions.like("expiredTime", filter));
                            dis.add(Restrictions.like("innerIps", filter));
                            dis.add(Restrictions.like("publicIps", filter));
                            dis.add(Restrictions.like("securityGroupIds", filter));
                            dis.add(Restrictions.like("serialNumber", filter));
                            criteria.add(dis);
                        } else {
                            criteria.add(Restrictions.eq(strings[0], e.getValue()));
                        }
                    } else if (strings[1].equals("lt")) {
                        criteria.add(Restrictions.lt(strings[0], e.getValue()));
                    } else if (strings[1].equals("gt")) {
                        criteria.add(Restrictions.gt(strings[0], e.getValue()));
                    } else if (strings[1].equals("like")) {
                        criteria.add(Restrictions.like(strings[0], e.getValue()));
                    }
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
                    String[] strings = e.getKey().split("@");
                    if (strings.length == 1) {
                        if (strings[0].equals("filter")) {
                            String filter = "%" + e.getValue() + "%";
                            // 多个or条件
                            Disjunction dis = Restrictions.disjunction();
                            dis.add(Restrictions.like("id", filter));
                            dis.add(Restrictions.like("accessKeyId", filter));
                            dis.add(Restrictions.like("instanceId", filter));
                            dis.add(Restrictions.like("instanceName", filter));
                            dis.add(Restrictions.like("instanceType", filter));
                            dis.add(Restrictions.like("instanceNetworkType", filter));
                            dis.add(Restrictions.like("hostName", filter));
                            dis.add(Restrictions.like("imageId", filter));
                            dis.add(Restrictions.like("regionId", filter));
                            dis.add(Restrictions.like("zoneId", filter));
                            dis.add(Restrictions.like("creationTime", filter));
                            dis.add(Restrictions.like("expiredTime", filter));
                            dis.add(Restrictions.like("innerIps", filter));
                            dis.add(Restrictions.like("publicIps", filter));
                            dis.add(Restrictions.like("securityGroupIds", filter));
                            dis.add(Restrictions.like("serialNumber", filter));
                            criteria.add(dis);
                        } else {
                            criteria.add(Restrictions.eq(strings[0], e.getValue()));
                        }
                    } else if (strings[1].equals("lt")) {
                        criteria.add(Restrictions.lt(strings[0], e.getValue()));
                    } else if (strings[1].equals("gt")) {
                        criteria.add(Restrictions.gt(strings[0], e.getValue()));
                    } else if (strings[1].equals("like")) {
                        criteria.add(Restrictions.like(strings[0], e.getValue()));
                    }
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
                    String[] strings = e.getKey().split("@");
                    if (strings.length == 1) {
                        if (strings[0].equals("filter")) {
                            String filter = "%" + e.getValue() + "%";
                            // 多个or条件
                            Disjunction dis = Restrictions.disjunction();
                            dis.add(Restrictions.like("id", filter));
                            dis.add(Restrictions.like("accessKeyId", filter));
                            dis.add(Restrictions.like("instanceId", filter));
                            dis.add(Restrictions.like("instanceName", filter));
                            dis.add(Restrictions.like("instanceType", filter));
                            dis.add(Restrictions.like("instanceNetworkType", filter));
                            dis.add(Restrictions.like("hostName", filter));
                            dis.add(Restrictions.like("imageId", filter));
                            dis.add(Restrictions.like("regionId", filter));
                            dis.add(Restrictions.like("zoneId", filter));
                            dis.add(Restrictions.like("creationTime", filter));
                            dis.add(Restrictions.like("expiredTime", filter));
                            dis.add(Restrictions.like("innerIps", filter));
                            dis.add(Restrictions.like("publicIps", filter));
                            dis.add(Restrictions.like("securityGroupIds", filter));
                            dis.add(Restrictions.like("serialNumber", filter));
                            criteria.add(dis);
                        } else {
                            criteria.add(Restrictions.eq(strings[0], e.getValue()));
                        }
                    } else if (strings[1].equals("lt")) {
                        criteria.add(Restrictions.lt(strings[0], e.getValue()));
                    } else if (strings[1].equals("gt")) {
                        criteria.add(Restrictions.gt(strings[0], e.getValue()));
                    } else if (strings[1].equals("like")) {
                        criteria.add(Restrictions.like(strings[0], e.getValue()));
                    }
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
