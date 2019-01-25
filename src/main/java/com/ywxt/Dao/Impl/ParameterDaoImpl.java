package com.ywxt.Dao.Impl;

import com.ywxt.Dao.CommonDao;
import com.ywxt.Dao.ParameterDao;
import com.ywxt.Domain.Parameter;
import org.hibernate.Criteria;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.HashMap;
import java.util.List;

public class ParameterDaoImpl extends CommonDao implements ParameterDao {

    private String domain = "Parameter";

    // get
    public Parameter getParameter(String key) {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Parameter> criteriaQuery = criteriaBuilder.createQuery(Parameter.class);
        Root<Parameter> from = criteriaQuery.from(Parameter.class);
        // 设置查询属性
        criteriaQuery.select(from).where(from.get("key").in(key));
        List<Parameter> list = session.createQuery(criteriaQuery).getResultList();
        if (list.size() == 0) {
            return null;
        }
        Parameter parameter = (Parameter) list.get(0);
        this.closeSession();
        return parameter;
    }

    // get
    public Parameter getParameter(int id) {
        try {
            session.beginTransaction();
            Parameter parameter = session.get(Parameter.class, id);
            session.getTransaction().commit();
            return parameter;
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            this.closeSession();
        }
    }

    // 新增/修改
    public int save(Parameter parameter) throws Exception {
        try {
            session.beginTransaction();
            if (parameter.getId() == 0) {
                int id = (Integer) session.save(parameter);
                session.getTransaction().commit();
                return id;
            } else {
                session.update(parameter);
                session.getTransaction().commit();
                return parameter.getId();
            }
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            this.closeSession();
        }
    }

    // 批量获取
    public List<Parameter> getList(HashMap<String, Object> params) throws Exception {
        Criteria criteria = this.getCriteria(Parameter.class, params);
        List<Parameter> list = criteria.list();
        this.closeSession();
        return list;
    }

    // 删除
    public void delete(int id) {
        String hqlDelete = "delete " + this.domain + " where id = :id";
        try {
            session.beginTransaction();
            session.createQuery(hqlDelete)
                    .setParameter("id", id)
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            this.closeSession();
        }
    }

}
