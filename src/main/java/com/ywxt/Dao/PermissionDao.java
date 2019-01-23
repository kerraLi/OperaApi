package com.ywxt.Dao;

import com.ywxt.Domain.Permission;
import com.ywxt.Utils.PageBean;
import org.hibernate.criterion.DetachedCriteria;

import java.io.Serializable;
import java.util.List;

public interface PermissionDao extends IBaseDao<Permission>{

    List<Permission> list();
}
