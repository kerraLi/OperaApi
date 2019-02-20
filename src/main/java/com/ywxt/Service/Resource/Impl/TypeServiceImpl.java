package com.ywxt.Service.Resource.Impl;

import com.ywxt.Dao.Resource.TypeDao;
import com.ywxt.Domain.Resource.Type;
import com.ywxt.Service.Resource.TypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("typeService")
public class TypeServiceImpl implements TypeService {

    @Resource
    private TypeDao typeDao;

    public int create(Type type) {
        return typeDao.create(type);
    }

    public boolean remove(int id) {
        return typeDao.delete(id);
    }

    public Type update(Type type) {
        return typeDao.update(type);
    }

    public Type getType(int id) {
        return typeDao.getType(id);
    }

    public Type getType(String code) {
        return typeDao.getType(code);
    }

    public Type save(Type type) {
        if (type.getId() == null) {
            int id = this.create(type);
            return this.getType(id);
        } else {
            return typeDao.update(type);
        }
    }

    public List<Type> getList() {
        return typeDao.getList();
    }
}
