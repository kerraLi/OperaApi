package com.ywxt.Service.Resource;

import com.ywxt.Domain.Resource.Type;

import java.util.List;

public interface TypeService {

    public int create(Type type);

    public boolean remove(int id);

    public Type update(Type type);

    public Type getType(int id);

    public Type getType(String code);

    public Type save(Type type);

    public List<Type> getList();
}
