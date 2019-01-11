package com.ywxt.Service.Impl;

import com.ywxt.Annotation.MarkCloumn;
import com.ywxt.Dao.Impl.ParameterIgnoreDaoImpl;
import com.ywxt.Domain.ParameterIgnore;
import com.ywxt.Service.ParameterIgnoreService;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ParameterIgnoreServiceImpl implements ParameterIgnoreService {


    // 设置标记
    public void saveMarked(Object object) throws Exception {
        Class c = object.getClass();
        ParameterIgnore pi = new ParameterIgnore();
        String[] cloumns = this.getMarkCloumn(object);
        pi.setDomain(c.getSimpleName());
        pi.setMarkKey(cloumns[0]);
        pi.setMarkValue(cloumns[1]);
        new ParameterIgnoreDaoImpl().save(pi);
    }

    // 查看是否被标记
    public boolean checkIfMarked(Object object) throws Exception {
        ParameterIgnore pi = new ParameterIgnore();
        String[] cloumns = this.getMarkCloumn(object);
        pi.setDomain(object.getClass().getSimpleName());
        pi.setMarkKey(cloumns[0]);
        pi.setMarkValue(cloumns[1]);
        ParameterIgnore resultPi = new ParameterIgnoreDaoImpl().getOne(pi);
        return !(resultPi == null);
    }

    // 删除标记
    public void deleteMarked(Object object) throws Exception {
        Class c = object.getClass();
        ParameterIgnore pi = new ParameterIgnore();
        String[] cloumns = this.getMarkCloumn(object);
        pi.setDomain(c.getSimpleName());
        pi.setMarkKey(cloumns[0]);
        pi.setMarkValue(cloumns[1]);
        new ParameterIgnoreDaoImpl().delete(pi);
    }

    // 批量获取标记数据
    public String[] getMarkedValues(Class c) {
        String domain = c.getSimpleName();
        String markedColumn = "";
        Field[] fields = c.getDeclaredFields();
        for (Field f : fields) {
            if (f.isAnnotationPresent(MarkCloumn.class)) {
                MarkCloumn markCloumn = f.getAnnotation(MarkCloumn.class);
                if (markCloumn.ifMarked()) {
                    markedColumn = f.getName();
                }
            }
        }
        return new ParameterIgnoreDaoImpl().getValues(domain, markedColumn);
    }

    // 获取标记key
    public String getMarkKey(Class c) {
        Field[] fields = c.getDeclaredFields();
        String markedColumn = "";
        for (Field f : fields) {
            if (f.isAnnotationPresent(MarkCloumn.class)) {
                MarkCloumn markCloumn = f.getAnnotation(MarkCloumn.class);
                if (markCloumn.ifMarked()) {
                    markedColumn = f.getName();
                }
            }
        }
        return markedColumn;
    }


    private String[] getMarkCloumn(Object object) throws Exception {
        String markedColumn = "";
        String markedValue = "";
        Class c = object.getClass();
        Field[] fields = c.getDeclaredFields();
        for (Field f : fields) {
            if (f.isAnnotationPresent(MarkCloumn.class)) {
                MarkCloumn markCloumn = f.getAnnotation(MarkCloumn.class);
                if (markCloumn.ifMarked()) {
                    PropertyDescriptor pd = new PropertyDescriptor(f.getName(), c);
                    Method readMethod = pd.getReadMethod();
                    markedColumn = f.getName();
                    markedValue = (String) readMethod.invoke(object);
                }
            }
        }
        String[] strings = new String[2];
        strings[0] = markedColumn;
        strings[1] = markedValue;
        return strings;
    }

}
