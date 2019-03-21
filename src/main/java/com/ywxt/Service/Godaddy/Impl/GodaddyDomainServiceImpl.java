package com.ywxt.Service.Godaddy.Impl;

import com.ywxt.Dao.Godaddy.GodaddyDomainDao;
import com.ywxt.Domain.Godaddy.GodaddyAccount;
import com.ywxt.Domain.Godaddy.GodaddyDomain;
import com.ywxt.Service.Godaddy.GodaddyDomainService;
import com.ywxt.Service.Godaddy.GodaddyService;
import com.ywxt.Service.System.IgnoreService;
import com.ywxt.Service.System.Impl.ParameterServiceImpl;
import com.ywxt.Service.System.ParameterService;
import com.ywxt.Utils.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.Transient;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.util.*;

@Service
public class GodaddyDomainServiceImpl implements GodaddyDomainService {

    private String paramIgnoreDomain = "GodaddyDomain";
    private String paramIgnoreColumn = "domainId";
    private String paramExpiresColumn = "expires";
    private String paramExpiresKey = "GODADDY_DOMAIN_EXPIRED_DAY";
    private String paramStatusColumn = "status";
    private String paramStatusNormal = "ACTIVE";
    private String[] ParamStatusExcept = {"ACTIVE"};

    @Autowired
    private GodaddyDomainDao godaddyDomainDao;
    @Autowired
    private IgnoreService ignoreService;
    @Autowired
    private ParameterService parameterService;
    @Autowired
    private GodaddyService godaddyService;

    // domain-查询报警域名
    public List<GodaddyDomain> getAlertList() {
        Specification<GodaddyDomain> specification = new Specification<GodaddyDomain>() {
            @Override
            public Predicate toPredicate(Root<GodaddyDomain> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                // 忽略数据
                String[] markValues = ignoreService.getMarkedValues(paramIgnoreDomain);
                if (markValues.length > 0) {
                    for (String s : markValues) {
                        Predicate predicate = cb.notEqual(root.get(paramIgnoreColumn).as(String.class), s);
                        predicates.add(predicate);
                    }
                }
                // 过期数据
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, Integer.parseInt(parameterService.getValue(paramExpiresKey)));
                Date thresholdDate = calendar.getTime();
                predicates.add(cb.equal(root.get(paramStatusColumn).as(String.class), paramStatusNormal));
                predicates.add(cb.lessThanOrEqualTo(root.get(paramExpiresColumn), thresholdDate));
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        return godaddyDomainDao.findAll(specification);
    }

    // domain-查询所有域名&分页
    public Page<GodaddyDomain> getList(Map<String, String> params) throws Exception {
        int pageNumber = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 1;
        int pageSize = params.containsKey("limit") ? Integer.parseInt(params.get("limit")) : 10;
        // 排除忽略数据
        String[] markValues = ignoreService.getMarkedValues(paramIgnoreDomain);
        // 处理过期数据
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, Integer.parseInt(parameterService.getValue(paramExpiresKey)));
        Date thresholdDate = calendar.getTime();
        Specification<GodaddyDomain> specification = new Specification<GodaddyDomain>() {
            @Override
            public Predicate toPredicate(Root<GodaddyDomain> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                // filter批量过滤
                if (params.containsKey("key")) {
                    String filter = "%" + params.get("key") + "%";
                    Field[] fields = GodaddyDomain.class.getDeclaredFields();
                    // 多个or条件
                    List<Predicate> psOr = new ArrayList<>();
                    for (Field f : fields) {
                        if (f.getType() == String.class && !f.isAnnotationPresent(Transient.class)) {
                            psOr.add(cb.like(root.get(f.getName()).as(String.class), filter));
                        }
                    }
                    predicates.add(cb.or(psOr.toArray(new Predicate[psOr.size()])));
                }
                // 忽略数据
                if (markValues.length > 0) {
                    if (params.containsKey("ifMarked") && params.get("ifMarked").equals("true")) {
                        // 多个or条件
                        List<Predicate> psOr = new ArrayList<>();
                        for (String s : markValues) {
                            psOr.add(cb.like(root.get(paramIgnoreColumn).as(String.class), s));
                        }
                        predicates.add(cb.or(psOr.toArray(new Predicate[psOr.size()])));
                    } else {
                        for (String s : markValues) {
                            Predicate predicate = cb.notEqual(root.get(paramIgnoreColumn).as(String.class), s);
                            predicates.add(predicate);
                        }
                    }
                }
                // 过期数据
                if (params.containsKey("ifExpired") && params.get("ifExpired").equals("true")) {
                    predicates.add(cb.equal(root.get(paramStatusColumn).as(String.class), paramStatusNormal));
                    predicates.add(cb.lessThanOrEqualTo(root.get(paramExpiresColumn), thresholdDate));
                    cb.asc(root.get(paramExpiresColumn));
                }
                // status
                if (params.containsKey("status")) {
                    if (params.get("status").equals("OTHERS")) {
                        for (String s : ParamStatusExcept) {
                            predicates.add(cb.notEqual(root.get(paramStatusColumn).as(String.class), s));
                        }
                    } else {
                        predicates.add(cb.equal(root.get(paramStatusColumn).as(String.class), params.get("status")));
                    }
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<GodaddyDomain> page = godaddyDomainDao.findAll(specification, pageable);
        // 处理查询条件
        for (GodaddyDomain gd : page.getContent()) {
            // 过期
            if (gd.getStatus().equals(paramStatusNormal)) {
                gd.setAlertExpired(gd.getExpires().before(thresholdDate));
            }
            // 弃用
            if (ArrayUtils.hasString(markValues, gd.getDomainId())) {
                gd.setAlertMarked(true);
            }
            gd.setUserName(godaddyService.getUserName(gd.getAccessKeyId()));
        }
        return page;
    }

}
