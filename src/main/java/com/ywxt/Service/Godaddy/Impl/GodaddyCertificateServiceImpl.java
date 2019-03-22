package com.ywxt.Service.Godaddy.Impl;

import com.ywxt.Dao.Godaddy.GodaddyCertificateDao;
import com.ywxt.Domain.Godaddy.GodaddyCertificate;
import com.ywxt.Service.Godaddy.GodaddyCertificateService;
import com.ywxt.Service.Godaddy.GodaddyService;
import com.ywxt.Service.System.IgnoreService;
import com.ywxt.Service.System.ParameterService;
import com.ywxt.Utils.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
public class GodaddyCertificateServiceImpl implements GodaddyCertificateService {

    private String paramIgnoreDomain = "GodaddyCertificate";
    private String paramIgnoreColumn = "certificateId";
    private String paramExpiresColumn = "validEnd";
    private String paramExpiresKey = "GODADDY_CERTIFICATE_EXPIRED_DAY";
    private String paramStatusColumn = "certificateStatus";
    private String paramStatusNormal = "ISSUED";
    private String[] ParamStatusExcept = {"ISSUED"};

    @Autowired
    private GodaddyCertificateDao godaddyCertificateDao;
    @Autowired
    private GodaddyService godaddyService;
    @Autowired
    private IgnoreService ignoreService;
    @Autowired
    private ParameterService parameterService;

    // certificates-查询报警证书
    public List<GodaddyCertificate> getAlertList() {
        Specification<GodaddyCertificate> specification = new Specification<GodaddyCertificate>() {
            @Override
            public Predicate toPredicate(Root<GodaddyCertificate> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
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
        return godaddyCertificateDao.findAll(specification);
    }

    // certificates-查询所有证书&分页
    public Page<GodaddyCertificate> getList(Map<String, String> params) throws Exception {
        int pageNumber = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 1;
        int pageSize = params.containsKey("limit") ? Integer.parseInt(params.get("limit")) : 10;
        // 排除忽略数据
        String[] markValues = ignoreService.getMarkedValues(paramIgnoreDomain);
        // 处理过期数据
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, Integer.parseInt(parameterService.getValue(paramExpiresKey)));
        Date thresholdDate = calendar.getTime();
        Specification<GodaddyCertificate> specification = new Specification<GodaddyCertificate>() {
            @Override
            public Predicate toPredicate(Root<GodaddyCertificate> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                // filter批量过滤
                if (params.containsKey("key")) {
                    String filter = "%" + params.get("key") + "%";
                    Field[] fields = GodaddyCertificate.class.getDeclaredFields();
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
                if (params.containsKey("ifMarked") && params.get("ifMarked").equals("true")) {
                    // in
                    List<Predicate> psOr = new ArrayList<>();
                    CriteriaBuilder.In<String> in = cb.in(root.get(paramIgnoreColumn));
                    if (markValues.length == 0) {
                        in.value((String) null);
                    } else {
                        for (String s : markValues) {
                            in.value(s);
                        }
                    }
                    predicates.add(in);
                } else {
                    for (String s : markValues) {
                        predicates.add(cb.notEqual(root.get(paramIgnoreColumn).as(String.class), s));
                    }
                }
                // 过期数据
                if (params.containsKey("ifExpired") && params.get("ifExpired").equals("true")) {
                    predicates.add(cb.equal(root.get(paramStatusColumn).as(String.class), paramStatusNormal));
                    predicates.add(cb.lessThanOrEqualTo(root.get(paramExpiresColumn), thresholdDate));
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
        Pageable pageable = null;
        if (params.containsKey("ifExpired") && params.get("ifExpired").equals("true")) {
            Sort sort = new Sort(Sort.Direction.ASC, paramExpiresColumn);
            pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
        } else {
            pageable = PageRequest.of(pageNumber - 1, pageSize);
        }
        Page<GodaddyCertificate> page = godaddyCertificateDao.findAll(specification, pageable);
        // 处理查询条件
        for (GodaddyCertificate gc : page.getContent()) {
            // 过期
            if (gc.getCertificateStatus().equals(paramStatusNormal)) {
                gc.setAlertExpired(gc.getValidEnd().before(thresholdDate));
            }
            // 弃用
            if (ArrayUtils.hasString(markValues, gc.getCertificateId())) {
                gc.setAlertMarked(true);
            }
            gc.setUserName(godaddyService.getUserName(gc.getAccessKeyId()));
        }
        return page;
    }

}
