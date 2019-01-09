package com.ywxt.Dao.Godaddy.Impl;

import com.ywxt.Dao.Godaddy.GodaddyDomainDao;
import com.ywxt.Domain.Godaddy.GodaddyDomain;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GodaddyDomainDaoImpl implements GodaddyDomainDao {

    private SessionFactory sessionFactory;
    private String domain = "GodaddyDomain";

    public GodaddyDomainDaoImpl() {
        Configuration configuration = new Configuration();
        this.sessionFactory = configuration.configure().buildSessionFactory();
    }

    // 获取数量
    public int getDomainTotal(HashMap<String, Object> params) {
        Session session = this.sessionFactory.openSession();
        Criteria criteria = session.createCriteria(GodaddyDomain.class);
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
                            dis.add(Restrictions.like("domain", filter));
                            dis.add(Restrictions.like("domainId", filter));
                            dis.add(Restrictions.like("createdAt", filter));
                            dis.add(Restrictions.like("expires", filter));
                            dis.add(Restrictions.like("renewDeadline", filter));
                            dis.add(Restrictions.like("status", filter));
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
    public List<GodaddyDomain> getDomainList(HashMap<String, Object> params) {
        Session session = this.sessionFactory.openSession();
        Criteria criteria = session.createCriteria(GodaddyDomain.class);
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
                            dis.add(Restrictions.like("domain", filter));
                            dis.add(Restrictions.like("domainId", filter));
                            dis.add(Restrictions.like("createdAt", filter));
                            dis.add(Restrictions.like("expires", filter));
                            dis.add(Restrictions.like("renewDeadline", filter));
                            dis.add(Restrictions.like("status", filter));
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
        List<GodaddyDomain> list = criteria.list();
        session.close();
        return list;
    }

    // 分页查找
    public List<GodaddyDomain> getDomainList(HashMap<String, Object> params, int pageNumber, int pageSize) {
        Session session = this.sessionFactory.openSession();
        Criteria criteria = session.createCriteria(GodaddyDomain.class);
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
                            dis.add(Restrictions.like("domain", filter));
                            dis.add(Restrictions.like("domainId", filter));
                            dis.add(Restrictions.like("createdAt", filter));
                            dis.add(Restrictions.like("expires", filter));
                            dis.add(Restrictions.like("renewDeadline", filter));
                            dis.add(Restrictions.like("status", filter));
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
        List<GodaddyDomain> list = criteria.list();
        session.close();
        return list;
    }

    // 批量保存
    public void saveDomains(List<GodaddyDomain> list) {
        Session session = this.sessionFactory.openSession();
        for (GodaddyDomain godaddyDomain : list) {
            session.save(godaddyDomain);
        }
        session.close();
    }

    // 保存更新
    public int saveDomain(GodaddyDomain godaddyDomain) {
        Session session = this.sessionFactory.openSession();
        int id = (int) session.save(godaddyDomain);
        session.close();
        return id;
    }

    // 删除
    public void deleteDomainByAccessId(String accessId) {
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
