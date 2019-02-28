package com.ywxt.Dao.Impl;

import com.ywxt.Dao.CommonDao;
import com.ywxt.Dao.ParameterIgnoreDao;
import com.ywxt.Domain.ParameterIgnore;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class ParameterIgnoreDaoImpl extends CommonDao implements ParameterIgnoreDao {

    private String domain = "ParameterIgnore";

    // 新增/修改
    public int save(ParameterIgnore parameterIgnore) throws Exception {
        try {
            session.beginTransaction();
            if (parameterIgnore.getId() == 0) {
                int id = (Integer) session.save(parameterIgnore);
                session.getTransaction().commit();
                return id;
            } else {
                session.update(parameterIgnore);
                session.getTransaction().commit();
                return parameterIgnore.getId();
            }
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            this.closeSession();
        }
    }


    // 获取标记
    public ParameterIgnore getOne(ParameterIgnore parameterIgnore) {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<ParameterIgnore> criteriaQuery = criteriaBuilder.createQuery(ParameterIgnore.class);
        Root<ParameterIgnore> from = criteriaQuery.from(ParameterIgnore.class);
        // 设置查询属性
        criteriaQuery.select(from).where(from.get("domain").in(parameterIgnore.getDomain()));
        criteriaQuery.select(from).where(from.get("markKey").in(parameterIgnore.getMarkKey()));
        criteriaQuery.select(from).where(from.get("markValue").in(parameterIgnore.getMarkValue()));
        List<ParameterIgnore> list = session.createQuery(criteriaQuery).getResultList();
        if (list.size() == 0) {
            return null;
        }
        ParameterIgnore p = (ParameterIgnore) list.get(0);
        this.closeSession();
        return p;
    }

    // 获取被标记值
    public String[] getValues(String domain, String markeKey) {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<ParameterIgnore> criteriaQuery = criteriaBuilder.createQuery(ParameterIgnore.class);
        Root<ParameterIgnore> from = criteriaQuery.from(ParameterIgnore.class);
        // 设置查询属性
        criteriaQuery.select(from).where(from.get("domain").in(domain));
        criteriaQuery.select(from).where(from.get("markKey").in(markeKey));
        List<ParameterIgnore> list = session.createQuery(criteriaQuery).getResultList();
        String[] strings = new String[list.size()];
        int i = 0;
        for (ParameterIgnore pi : list) {
            strings[i] = pi.getMarkValue();
            i++;
        }
        this.closeSession();
        return strings;
    }

    // 删除
    public void delete(ParameterIgnore parameterIgnore) {
        String hqlDelete = "delete " + this.domain + " where" +
                " domain = :domain and" +
                " markKey = :markKey and" +
                " markValue = :markValue";
        try {
            session.beginTransaction();
            session.createQuery(hqlDelete)
                    .setParameter("domain", parameterIgnore.getDomain())
                    .setParameter("markKey", parameterIgnore.getMarkKey())
                    .setParameter("markValue", parameterIgnore.getMarkValue())
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            this.closeSession();
        }
    }

}
