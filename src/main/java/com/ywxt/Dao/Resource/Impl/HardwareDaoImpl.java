package com.ywxt.Dao.Resource.Impl;

import com.ywxt.Dao.CommonDao;
import com.ywxt.Dao.Resource.HardwareDao;
import com.ywxt.Domain.Resource.Hardware;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;

import java.util.HashMap;
import java.util.List;

public class HardwareDaoImpl extends CommonDao implements HardwareDao {

    private String domain = "Hardware";

    // get
    public Hardware getParameter(int id) {
        try {
            session.beginTransaction();
            Hardware hardware = session.get(Hardware.class, id);
            session.getTransaction().commit();
            return hardware;
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            this.closeSession();
        }
    }

    // 新增/修改
    public int save(Hardware hardware) throws Exception {
        try {
            session.beginTransaction();
            if (hardware.getId() == 0) {
                int id = (Integer) session.save(hardware);
                session.getTransaction().commit();
                return id;
            } else {
                session.update(hardware);
                session.getTransaction().commit();
                return hardware.getId();
            }
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            this.closeSession();
        }
    }

    // 批量保存
    public void saveAll(List<Hardware> list) {
        for (Hardware hardware : list) {
            session.save(hardware);
        }
        this.closeSession();
    }


    // 获取数量
    public int getTotal(HashMap<String, Object> params) throws Exception {
        Criteria criteria = this.getCriteria(Hardware.class, params);
        criteria.setProjection(Projections.rowCount());
        int total = Integer.parseInt(criteria.uniqueResult().toString());
        this.closeSession();
        return total;
    }

    // 批量获取
    public List<Hardware> getList(HashMap<String, Object> params) throws Exception {
        Criteria criteria = this.getCriteria(Hardware.class, params);
        List<Hardware> list = criteria.list();
        this.closeSession();
        return list;
    }

    // 分页查找
    public List<Hardware> getList(HashMap<String, Object> params, int pageNumber, int pageSize) throws Exception {
        Criteria criteria = this.getCriteria(Hardware.class, params);
        criteria.setFirstResult((pageNumber - 1) * pageSize);
        criteria.setMaxResults(pageSize);
        List<Hardware> list = criteria.list();
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

    // 批量删除
    public void deleteAll(List<Integer> ids) {
        String hqlDelete = "delete " + this.domain + " where id in (:ids)";
        try {
            session.beginTransaction();
            int updatedEntities = session.createQuery(hqlDelete)
                    .setParameterList("ids", ids)
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            this.closeSession();
        }
    }
}
