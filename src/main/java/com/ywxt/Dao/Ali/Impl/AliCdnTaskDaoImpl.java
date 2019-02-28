package com.ywxt.Dao.Ali.Impl;

import com.ywxt.Dao.Ali.AliCdnTaskDao;
import com.ywxt.Dao.CommonDao;
import com.ywxt.Domain.Ali.AliCdnTask;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;

import java.util.HashMap;
import java.util.List;

public class AliCdnTaskDaoImpl extends CommonDao implements AliCdnTaskDao {

    public AliCdnTask getCdnTask(int id) {
        try {
            session.beginTransaction();
            AliCdnTask aliCdnTask = (AliCdnTask) session.get(AliCdnTask.class, id);
            session.getTransaction().commit();
            return aliCdnTask;
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            this.closeSession();
        }
    }

    public AliCdnTask getCdnTask(String taskId) throws Exception {
        Criteria criteria = this.getCriteria(AliCdnTask.class, new HashMap<>() {{
            put("taskId", taskId);
        }});
        return (AliCdnTask) criteria.uniqueResult();
    }

    // 保存更新
    public int saveCdnTask(AliCdnTask aliCdnTask) {
        int id;
        if (aliCdnTask.getId() != 0) {
            id = aliCdnTask.getId();
            try {
                session.beginTransaction();
                session.update(aliCdnTask);
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
            } finally {
                this.closeSession();
            }
        } else {
            id = (int) session.save(aliCdnTask);
            this.closeSession();
        }
        return id;
    }

    // 获取数量
    public int getTotal(HashMap<String, Object> params) throws Exception {
        Criteria criteria = getCriteria(AliCdnTask.class, params);
        criteria.setProjection(Projections.rowCount());
        int total = Integer.parseInt(criteria.uniqueResult().toString());
        this.closeSession();
        return total;
    }

    // 分页查找
    public List<AliCdnTask> getList(HashMap<String, Object> params, int pageNumber, int pageSize) throws Exception {
        Criteria criteria = getCriteria(AliCdnTask.class, params);
        criteria.setFirstResult((pageNumber - 1) * pageSize);
        criteria.setMaxResults(pageSize);
        List<AliCdnTask> list = criteria.list();
        this.closeSession();
        return list;
    }


}
