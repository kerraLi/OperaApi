package com.ywxt.Service.User.Repository;

import com.ywxt.Domain.User.Dto.UserRelationDTO;
import com.ywxt.Domain.User.User;
import com.ywxt.Utils.ArrayUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserRepository {

    @Resource
    @PersistenceContext
    private EntityManager em;

    public User getUserWithRoles(Long userId) throws Exception {
        List<UserRelationDTO> list = this.getDtos("menu", userId);
        return this.dtosToUser(list);
    }

    private List<UserRelationDTO> getDtos(String permissionType, Long userId) {
        List<UserRelationDTO> dtos = em.createQuery(
                "select new " +
                        "   com.ywxt.Domain.User.Dto.UserRelationDTO(" +
                        "       urp.id,u.id,ur.id,up.id," +
                        "       u.username,u.nickname,u.introduction,u.avatar," +
                        "       ur.code,up.action,up.type" +
                        "   ) " +
                        "from UserRelation urp " +
                        "join urp.user u " +
                        "join urp.userRole ur " +
                        "join urp.userPermission up " +
                        "where up.type = :permissionType " +
                        "and u.id = :userId", UserRelationDTO.class)
                .setParameter("permissionType", permissionType)
                .setParameter("userId", userId)
                .getResultList();
        return dtos;
    }


    private User dtosToUser(List<UserRelationDTO> list) throws Exception {
        User user = new User();
        if (list.size() == 0) {
            // 权限错误:没有菜单权限
            throw new Exception("403");
        }
        UserRelationDTO temp = list.get(0);
        user.setId(temp.getUserId());
        user.setUsername(temp.getUsername());
        user.setNickname(temp.getNickname());
        user.setIntroduction(temp.getIntroduction());
        user.setAvatar(temp.getAvatar());
        List<String> rolesCodes = new ArrayList<>();
        List<String> permissionMenus = new ArrayList<>();
        for (UserRelationDTO dto : list) {
            if (!rolesCodes.contains(dto.getRoleCode())) {
                rolesCodes.add(dto.getRoleCode());
            }
            if (dto.getPermissionType().equals("menu")
                    && !permissionMenus.contains(dto.getPermissionAction())) {
                permissionMenus.add(dto.getPermissionAction());
            }
        }
        user.setRoles(rolesCodes.toArray(new String[rolesCodes.size()]));
        user.setMenus(permissionMenus.toArray(new String[permissionMenus.size()]));
        return user;
    }

}
