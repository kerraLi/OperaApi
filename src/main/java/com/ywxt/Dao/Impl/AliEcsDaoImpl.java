package com.ywxt.Dao.Impl;

import com.ywxt.Dao.AliEcsDao;
import com.ywxt.Domain.AliEcs;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class AliEcsDaoImpl implements AliEcsDao {

    private SessionFactory sessionFactory;
    private String tableName = "ali_ecs";

    public AliEcsDaoImpl() {
        Configuration configuration = new Configuration();
        this.sessionFactory = configuration.configure().buildSessionFactory();
    }

    // 批量保存
    public void saveAliEcses(List<AliEcs> list) {
        Session session = this.sessionFactory.openSession();
        for (AliEcs aliEcs : list) {
            session.save(aliEcs);
        }
        session.close();
    }

    // 保存更新
    public int saveAliEcs(AliEcs aliEcs) {
        Session session = this.sessionFactory.openSession();
        int id = (int) session.save(aliEcs);
        session.close();
        return id;
    }

    // 删除
    public void deleteAliEcsByAccessId(String accessId) {
        Session session = this.sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        String hqlDelete = "delete AliEcs where accessKeyId = :accessKeyId";
        session.createQuery(hqlDelete)
                .setParameter("accessKeyId", accessId)
                .executeUpdate();
        tx.commit();
        session.close();
    }
}
