package com.ywxt.Dao.User;

import com.ywxt.Domain.User.User;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public interface UserDao {

    public Long create(User user);

    public boolean delete(long id);

    public boolean delete(User user);

    public User update(User user);

    public User getUser(long id);

    public User getUser(String username) throws Exception;

    public List<User> getList(HashMap<String, Object> params, int pageNumber, int pageSize);

    public int getListTotal(HashMap<String, Object> params);

}
