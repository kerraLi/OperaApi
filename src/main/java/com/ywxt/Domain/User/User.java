package com.ywxt.Domain.User;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Data
@Entity
@Table(name = "users")
@DynamicInsert
@DynamicUpdate
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String nickname;
    private String introduction;
    private String avatar;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "roleId")
    private UserRole role;
    @Transient
    private String[] roles = new String[0];
    @Transient
    private String[] menus = new String[0];

}
