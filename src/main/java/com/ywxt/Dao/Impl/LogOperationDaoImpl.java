package com.ywxt.Dao.Impl;

import com.ywxt.Dao.CommonDao;
import com.ywxt.Dao.LogOperationDao;
import com.ywxt.Domain.LogOperation;
import org.springframework.stereotype.Repository;

@Repository("logOperationDao")
public class LogOperationDaoImpl extends CommonDao implements LogOperationDao {

    public int save(LogOperation logOperation) {
        try {
            session.beginTransaction();
            if (logOperation.getId() == null || logOperation.getId() == 0) {
                int id = (Integer) session.save(logOperation);
                session.getTransaction().commit();
                return id;
            } else {
                session.update(logOperation);
                session.getTransaction().commit();
                return logOperation.getId();
            }
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            this.closeSession();
        }
    }
}
