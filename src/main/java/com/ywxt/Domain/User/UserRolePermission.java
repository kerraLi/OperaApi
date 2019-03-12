package com.ywxt.Domain.User;

import javax.persistence.*;

@Entity
@Table(name = "user_role_permission")
public class UserRolePermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roleId")
    private UserRole userRole;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permissionId")
    private UserPermission userPermission;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public UserPermission getUserPermission() {
        return userPermission;
    }

    public void setUserPermission(UserPermission userPermission) {
        this.userPermission = userPermission;
    }
}
