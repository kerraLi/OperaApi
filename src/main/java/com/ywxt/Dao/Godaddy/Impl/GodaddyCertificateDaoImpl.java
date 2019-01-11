package com.ywxt.Dao.Godaddy.Impl;

import com.ywxt.Dao.CommonDao;
import com.ywxt.Dao.Godaddy.GodaddyCertificateDao;
import com.ywxt.Domain.Godaddy.GodaddyCertificate;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;

import java.util.HashMap;
import java.util.List;

public class GodaddyCertificateDaoImpl extends CommonDao implements GodaddyCertificateDao {

    private String domain = "GodaddyCertificate";

    public GodaddyCertificate getCertificate(int id) {
        try {
            session.beginTransaction();
            GodaddyCertificate godaddyCertificate = (GodaddyCertificate) session.get(GodaddyCertificate.class, id);
            session.getTransaction().commit();
            return godaddyCertificate;
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            this.closeSession();
        }
    }
    // 获取数量
    public int getCertificateTotal(HashMap<String, Object> params) throws Exception {
        Criteria criteria = this.getCriteria(GodaddyCertificate.class, params);
        criteria.setProjection(Projections.rowCount());
        int total = Integer.parseInt(criteria.uniqueResult().toString());
        this.closeSession();
        return total;
    }

    // 查找所有
    public List<GodaddyCertificate> getCertificateList(HashMap<String, Object> params) throws Exception {
        Criteria criteria = this.getCriteria(GodaddyCertificate.class, params);
        List<GodaddyCertificate> list = criteria.list();
        this.closeSession();
        return list;
    }

    // 分页查找
    public List<GodaddyCertificate> getCertificateList(HashMap<String, Object> params, int pageNumber, int pageSize) throws Exception {
        Criteria criteria = this.getCriteria(GodaddyCertificate.class, params);
        criteria.setFirstResult((pageNumber - 1) * pageSize);
        criteria.setMaxResults(pageSize);
        List<GodaddyCertificate> list = criteria.list();
        this.closeSession();
        return list;
    }

    // 批量保存
    public void saveCertificates(List<GodaddyCertificate> list) {
        for (GodaddyCertificate godaddyCertificate : list) {
            session.save(godaddyCertificate);
        }
        this.closeSession();
    }

    // 保存更新
    public int saveCertificate(GodaddyCertificate godaddyCertificate) {
        int id = (int) session.save(godaddyCertificate);
        this.closeSession();
        return id;
    }

    // 删除
    public void deleteCertificateByAccessId(String accessId) {
        String hqlDelete = "delete " + this.domain + " where accessKeyId = :accessKeyId";
        try {
            session.beginTransaction();
            session.createQuery(hqlDelete)
                    .setParameter("accessKeyId", accessId)
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            this.closeSession();
        }
    }
}
