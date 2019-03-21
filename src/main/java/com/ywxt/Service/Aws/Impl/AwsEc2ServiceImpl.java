package com.ywxt.Service.Aws.Impl;

import com.ywxt.Dao.Aws.AwsEc2Dao;
import com.ywxt.Domain.Aws.AwsEc2;
import com.ywxt.Service.Aws.AwsEc2Service;
import com.ywxt.Service.Aws.AwsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import javax.persistence.Transient;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AwsEc2ServiceImpl implements AwsEc2Service {

    private String paramIgnoreDomain = "AwsEc2";
    private String paramIgnoreColumn = "instanceId";
    private String paramExpiresColumn = "";
    private String paramExpiresKey = "";
    private String paramStatusColumn = "status";
    private String paramStatusNormal = "running";
    private String[] ParamStatusExcept = {"running"};

    @Autowired
    private AwsEc2Dao awsEc2Dao;
    @Autowired
    private AwsService awsService;

    // ec2-查询所有实例的详细信息&分页
    public Page<AwsEc2> getList(Map<String, String> params) {
        int pageNumber = params.containsKey("page") ? 1 : Integer.parseInt(params.get("page"));
        int pageSize = params.containsKey("limit") ? 10 : Integer.parseInt(params.get("limit"));
        Specification<AwsEc2> specification = new Specification<AwsEc2>() {
            @Override
            public Predicate toPredicate(Root<AwsEc2> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                // filter批量过滤
                if (params.containsKey("key")) {
                    String filter = "%" + params.get("key") + "%";
                    Field[] fields = AwsEc2.class.getDeclaredFields();
                    // 多个or条件
                    List<Predicate> psOr = new ArrayList<>();
                    for (Field f : fields) {
                        if (f.getType() == String.class && !f.isAnnotationPresent(Transient.class)) {
                            psOr.add(cb.like(root.get(f.getName()).as(String.class), filter));
                        }
                    }
                    predicates.add(cb.or(psOr.toArray(new Predicate[psOr.size()])));
                }
                // status
                if (params.containsKey("status")) {
                    if (params.get("status").equals("others")) {
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
        Page<AwsEc2> page = awsEc2Dao.findAll(specification, pageable);
        // 查询后处理
        for (AwsEc2 ae : page.getContent()) {
            ae.setUserName(awsService.getUserName(ae.getAccessKeyId()));
        }
        return page;
    }

}
