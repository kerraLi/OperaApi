package com.ywxt.Dao.Impl;

import com.ywxt.Dao.CommonDao;
import com.ywxt.Dao.MessageDao;
import com.ywxt.Domain.Message;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;

import java.util.HashMap;
import java.util.List;

public class MessageDaoImpl extends CommonDao implements MessageDao {

    protected String domain = "Message";

    public int save(Message message) {
        try {
            session.beginTransaction();
            int id = (Integer) session.save(message);
            session.getTransaction().commit();
            return id;
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            this.closeSession();
        }
    }

    // 批量保存状态
    public void saveAliEcses(List<Integer> ids, String status) throws Exception {
        try {
            session.beginTransaction();
            String hql = "update Message set status = :status, modifyTime = current_time() where id in (:ids) and status != :status2";
            int updatedEntities = session.createQuery(hql)
                    .setParameter("status", status)
                    .setParameterList("ids", ids)
                    .setParameter("status2", status)
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            this.closeSession();
        }
    }

    public Message getById(int id) {
        try {
            session.beginTransaction();
            Message message = (Message) session.get(Message.class, id);
            session.getTransaction().commit();
            return message;
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            this.closeSession();
        }
    }

    // 个数
    public int getListTotal(HashMap<String, Object> params) throws Exception {
        Criteria criteria = this.getCriteria(Message.class, params);
        criteria.setProjection(Projections.rowCount());
        int total = Integer.parseInt(criteria.uniqueResult().toString());
        this.closeSession();
        return total;
    }

    // 分页查找
    public List<Message> getList(HashMap<String, Object> params, int pageNumber, int pageSize) throws Exception {
        Criteria criteria = this.getCriteria(Message.class, params);
        criteria.setFirstResult((pageNumber - 1) * pageSize);
        criteria.setMaxResults(pageSize);
        List<Message> list = criteria.list();
        this.closeSession();
        return list;
    }

}
