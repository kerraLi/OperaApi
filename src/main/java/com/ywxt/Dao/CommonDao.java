package com.ywxt.Dao;

import com.ywxt.Annotation.NotFilterCloumn;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import java.lang.reflect.Field;
import java.util.HashMap;

import java.util.Map;

public class CommonDao /*extends BaseDaoImpl*/{
/*    // ctrl + shift + x
    private static final Configuration CONFIG;
    private static final SessionFactory FACTORY;

    // 编写静态代码块
    static{
        // 加载XML的配置文件
        CONFIG = new Configuration().configure();
        // 构造工厂
        FACTORY = CONFIG.buildSessionFactory();
    }

    *//**
     * 从工厂中获取Session对象
     * @return
     *//*
    public static Session getSession(){
        return FACTORY.openSession();
    }

    *//**
     * // 从ThreadLocal类中获取到session的对象
     * @return
     *//*
    public static Session getCurrentSession(){
        return FACTORY.getCurrentSession();
    }*/
    protected SessionFactory sessionFactory;
    protected Session session;

    protected CommonDao() {
        Configuration configuration = new Configuration();
        this.sessionFactory = configuration.configure().buildSessionFactory();
        this.session = this.sessionFactory.openSession();
    }

    // 获取条件
    protected Criteria getCriteria(Class c, HashMap<String, Object> params) throws Exception {
        Criteria criteria = session.createCriteria(c);
        if (params != null) {
            for (Map.Entry<String, Object> e : params.entrySet()) {
                if (e.getKey().equals("orderAsc")) {
                    criteria.addOrder(Order.asc((String) e.getValue()));
                } else if (e.getKey().equals("orderDesc")) {
                    criteria.addOrder(Order.desc((String) e.getValue()));
                } else {
                    String[] strings = e.getKey().split("@");
                    if (strings.length == 1) {
                        if (strings[0].equals("filter")) {
                            String filter = "%" + (String) e.getValue() + "%";
                            Field[] fields = c.getDeclaredFields();
                            // 多个or条件 使用Disjunction
                            Disjunction dis = Restrictions.disjunction();
                            for (Field f : fields) {
                                if (f.getType() == String.class && !f.isAnnotationPresent(NotFilterCloumn.class)) {
                                    dis.add(Restrictions.like(f.getName(), filter));
                                }
                            }
                            criteria.add(dis);
                        } else {
                            criteria.add(Restrictions.eq(strings[0], e.getValue()));
                        }
                    } else if (strings[1].equals("in")) {
                        criteria.add(Restrictions.in(strings[0], (String[]) e.getValue()));
                    } else if (strings[1].equals("notIn")) {
                        criteria.add(Restrictions.not(Restrictions.in(strings[0], (String[]) e.getValue())));
                    } else if (strings[1].equals("eq")) {
                        criteria.add(Restrictions.eq(strings[0], e.getValue()));
                    } else if (strings[1].equals("ne")) {
                        criteria.add(Restrictions.ne(strings[0], e.getValue()));
                    } else if (strings[1].equals("lt")) {
                        criteria.add(Restrictions.lt(strings[0], e.getValue()));
                    } else if (strings[1].equals("gt")) {
                        criteria.add(Restrictions.gt(strings[0], e.getValue()));
                    } else if (strings[1].equals("like")) {
                        criteria.add(Restrictions.like(strings[0], e.getValue()));
                    }
                }
            }
        }
        criteria.addOrder(Order.asc("id"));
        return criteria;
    }

    // 关闭session
    protected void closeSession() {
        this.session.close();
        this.sessionFactory.close();
    }
}
