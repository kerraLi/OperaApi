package com.ywxt.Dao.User.Impl;

import com.ywxt.Dao.User.UserRolePermissionDao;
import com.ywxt.Domain.User.UserPermission;
import com.ywxt.Domain.User.UserRolePermission;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserRolePermissionDaoImpl implements UserRolePermissionDao {


    @PersistenceContext
    private EntityManager em;

    @Override
    public Long create(UserRolePermission userRolePermission) {
        em.persist(userRolePermission);
        return userRolePermission.getId();
    }

    @Override
    public boolean delete(String type, long id) {
        if (type.equals("id")) {
            em.remove(this.getUserRolePermission(id));
            return true;
        } else {
            em.createQuery(
                    "delete from UserRolePermission urp " +
                            "where urp.userRole in " +
                            "(" +
                            "select r.id from urp.userRole r " +
                            "where r.id=:roleId" +
                            ")")
                    .setParameter("roleId", id)
                    .executeUpdate();
            return true;
        }
    }

    @Override
    public boolean delete(UserRolePermission userRolePermission) {
        em.remove(userRolePermission);
        return true;
    }

    @Override
    public UserRolePermission update(UserRolePermission userRolePermission) {
        return null;
    }

    @Override
    public UserRolePermission getUserRolePermission(long id) {
        return em.find(UserRolePermission.class, id);
    }

    @Override
    public List<UserPermission> getRolePermissions(long roleId) {
        return em.createQuery(
                "select new " +
                        "   com.ywxt.Domain.User.UserPermission(" +
                        "       up.id,up.parentId,up.name,up.action,up.type" +
                        "   ) " +
                        "from UserRolePermission urp " +
                        "join urp.userRole ur " +
                        "join urp.userPermission up " +
                        "where ur.id = :roleId", UserPermission.class)
                .setParameter("roleId", roleId)
                .getResultList();
    }
}
