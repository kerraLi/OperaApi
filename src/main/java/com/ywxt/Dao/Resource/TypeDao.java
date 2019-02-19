package com.ywxt.Dao.Resource;

import com.ywxt.Domain.Resource.Type;

import java.util.List;

public interface TypeDao {

    public int create(Type type);

    public boolean delete(int id);

    public boolean delete(Type type);

    public Type update(Type type);

    public Type getType(int id);

    public List<Type> getList();
}
