package com.ywxt.Dao.Godaddy.Impl;

import com.ywxt.Dao.Godaddy.GodaddyCertificateDao;
import com.ywxt.Domain.Godaddy.GodaddyCertificate;
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

public class GodaddyCertificateDaoImpl implements GodaddyCertificateDao {

    private SessionFactory sessionFactory;
    private String domain = "GodaddyCertificate";

    public GodaddyCertificateDaoImpl() {
        Configuration configuration = new Configuration();
        this.sessionFactory = configuration.configure().buildSessionFactory();
    }

    // 获取数量
    public int getCertificateTotal(HashMap<String, Object> params) {
        Session session = this.sessionFactory.openSession();
        Criteria criteria = session.createCriteria(GodaddyCertificate.class);
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
                    } else if (strings[1].equals("ne")) {
                        criteria.add(Restrictions.ne(strings[0], e.getValue()));
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
    public List<GodaddyCertificate> getCertificateList(HashMap<String, Object> params) {
        Session session = this.sessionFactory.openSession();
        Criteria criteria = session.createCriteria(GodaddyCertificate.class);
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
                            dis.add(Restrictions.like("certificateId", filter));
                            dis.add(Restrictions.like("createdAt", filter));
                            dis.add(Restrictions.like("validStart", filter));
                            dis.add(Restrictions.like("validEnd", filter));
                            dis.add(Restrictions.like("productType", filter));
                            dis.add(Restrictions.like("serialNumberHex", filter));
                            dis.add(Restrictions.like("subjectAlternativeNames", filter));
                            dis.add(Restrictions.like("locale", filter));
                            dis.add(Restrictions.like("commonName", filter));
                            dis.add(Restrictions.like("serialNumber", filter));
                            dis.add(Restrictions.like("productGuid", filter));
                            criteria.add(dis);
                        } else {
                            criteria.add(Restrictions.eq(strings[0], e.getValue()));
                        }
                    } else if (strings[1].equals("ne")) {
                        criteria.add(Restrictions.ne(strings[0], e.getValue()));
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
        List<GodaddyCertificate> list = criteria.list();
        session.close();
        return list;
    }

    // 分页查找
    public List<GodaddyCertificate> getCertificateList(HashMap<String, Object> params, int pageNumber, int pageSize) {
        Session session = this.sessionFactory.openSession();
        Criteria criteria = session.createCriteria(GodaddyCertificate.class);
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
                            dis.add(Restrictions.like("certificateId", filter));
                            dis.add(Restrictions.like("createdAt", filter));
                            dis.add(Restrictions.like("validStart", filter));
                            dis.add(Restrictions.like("validEnd", filter));
                            dis.add(Restrictions.like("productType", filter));
                            dis.add(Restrictions.like("serialNumberHex", filter));
                            dis.add(Restrictions.like("subjectAlternativeNames", filter));
                            dis.add(Restrictions.like("locale", filter));
                            dis.add(Restrictions.like("commonName", filter));
                            dis.add(Restrictions.like("serialNumber", filter));
                            dis.add(Restrictions.like("productGuid", filter));
                            criteria.add(dis);
                        } else {
                            criteria.add(Restrictions.eq(strings[0], e.getValue()));
                        }
                    } else if (strings[1].equals("ne")) {
                        criteria.add(Restrictions.ne(strings[0], e.getValue()));
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
        List<GodaddyCertificate> list = criteria.list();
        session.close();
        return list;
    }

    // 批量保存
    public void saveCertificates(List<GodaddyCertificate> list) {
        Session session = this.sessionFactory.openSession();
        for (GodaddyCertificate godaddyCertificate : list) {
            session.save(godaddyCertificate);
        }
        session.close();
    }

    // 保存更新
    public int saveCertificate(GodaddyCertificate godaddyCertificate) {
        Session session = this.sessionFactory.openSession();
        int id = (int) session.save(godaddyCertificate);
        session.close();
        return id;
    }

    // 删除
    public void deleteCertificateByAccessId(String accessId) {
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
