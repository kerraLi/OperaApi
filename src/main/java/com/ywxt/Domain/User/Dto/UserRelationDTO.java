package com.ywxt.Domain.User.Dto;

public class UserRelationDTO {

    private long id;
    private long userId;
    private long roleId;
    private long permissionId;
    private String username;
    private String nickname;
    private String introduction;
    private String avatar;
    // 权限code
    private String roleCode;
    // 权限
    private String permissionAction;
    private String permissionType;

    public UserRelationDTO() {
    }

    public UserRelationDTO(
            long id, long userId, long roleId, long permissionId,
            String username, String nickname, String introduction, String avatar,
            String roleCode, String permissionAction, String permissionType
    ) {
        this.id = id;
        this.userId = userId;
        this.roleId = roleId;
        this.permissionId = permissionId;
        this.username = username;
        this.nickname = nickname;
        this.introduction = introduction;
        this.avatar = avatar;
        this.roleCode = roleCode;
        this.permissionAction = permissionAction;
        this.permissionType = permissionType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(long permissionId) {
        this.permissionId = permissionId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getPermissionAction() {
        return permissionAction;
    }

    public void setPermissionAction(String permissionAction) {
        this.permissionAction = permissionAction;
    }

    public String getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(String permissionType) {
        this.permissionType = permissionType;
    }
}
