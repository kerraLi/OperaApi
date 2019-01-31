package com.ywxt.Dao.Aws.Impl;

import com.ywxt.Dao.CommonDao;
import com.ywxt.Domain.Aws.AwsEc2;
import org.hibernate.Criteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;

import java.util.HashMap;
import java.util.List;

public class AwsEc2DaoImpl extends CommonDao {

    private String domain = "AwsEc2";

    public AwsEc2 getEc2(int id) {
        try {
            session.beginTransaction();
            AwsEc2 awsEc2 = (AwsEc2) session.get(AwsEc2.class, id);
            session.getTransaction().commit();
            return awsEc2;
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            this.closeSession();
        }
    }

    // group by 查找个数&account
    public List<Object[]> getCountGroup(HashMap<String, Object> params) throws Exception {
        // 数据库限制 group时无法使用order
        params.put("NO_ORDER", true);
        Criteria criteria = this.getCriteria(AwsEc2.class, params);
        ProjectionList projectionList = Projections.projectionList();
        // group by theme
        projectionList.add(Projections.groupProperty("status"));
        projectionList.add(Projections.groupProperty("accessKeyId"));
        projectionList.add(Projections.count("id"));
        criteria.setProjection(projectionList);
        List<Object[]> results = criteria.list();
        this.closeSession();
        return results;
    }

    // group by 查找个数&account
    public List<Object[]> getTotalByAccount(HashMap<String, Object> params) throws Exception {
        // 数据库限制 group时无法使用order
        params.put("NO_ORDER", true);
        Criteria criteria = this.getCriteria(AwsEc2.class, params);
        ProjectionList projectionList = Projections.projectionList();
        // group by theme
        projectionList.add(Projections.groupProperty("accessKeyId"));
        projectionList.add(Projections.count("id"));
        criteria.setProjection(projectionList);
        List<Object[]> results = criteria.list();
        this.closeSession();
        return results;
    }

    // 获取数量
    public int getTotal(HashMap<String, Object> params) throws Exception {
        Criteria criteria = this.getCriteria(AwsEc2.class, params);
        criteria.setProjection(Projections.rowCount());
        int total = Integer.parseInt(criteria.uniqueResult().toString());
        this.closeSession();
        return total;
    }

    // 查找所有
    public List<AwsEc2> getList(HashMap<String, Object> params) throws Exception {
        Criteria criteria = this.getCriteria(AwsEc2.class, params);
        List<AwsEc2> list = criteria.list();
        this.closeSession();
        return list;
    }

    // 分页查找
    public List<AwsEc2> getList(HashMap<String, Object> params, int pageNumber, int pageSize) throws Exception {
        Criteria criteria = this.getCriteria(AwsEc2.class, params);
        criteria.setFirstResult((pageNumber - 1) * pageSize);
        criteria.setMaxResults(pageSize);
        List<AwsEc2> list = criteria.list();
        this.closeSession();
        return list;
    }

    // 批量保存
    public void saveAwsEc2s(List<AwsEc2> list) {
        for (AwsEc2 awsEc2 : list) {
            session.save(awsEc2);
        }
        this.closeSession();
    }

    // 保存更新
    public int saveAwsEc2(AwsEc2 awsEc2) throws Exception {
        try {
            session.beginTransaction();
            if (awsEc2.getId() == 0) {
                int id = (Integer) session.save(awsEc2);
                session.getTransaction().commit();
                return id;
            } else {
                session.update(awsEc2);
                session.getTransaction().commit();
                return awsEc2.getId();
            }
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            this.closeSession();
        }
    }

    // 删除
    public void deleteAwsEc2ByAccessId(String accessId) {
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
