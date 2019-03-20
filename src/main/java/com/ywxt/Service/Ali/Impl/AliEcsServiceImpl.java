package com.ywxt.Service.Ali.Impl;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.*;
import com.ywxt.Dao.Ali.AliEcsDao;
import com.ywxt.Domain.Ali.AliEcs;
import com.ywxt.Service.Ali.AliEcsService;
import com.ywxt.Service.Ali.AliService;
import com.ywxt.Service.System.IgnoreService;
import com.ywxt.Service.System.ParameterService;
import com.ywxt.Utils.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
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
public class AliEcsServiceImpl implements AliEcsService {

    @Autowired
    private AliService aliService;
    @Autowired
    private AliEcsDao aliEcsDao;
    @Autowired
    private IgnoreService ignoreService;
    @Autowired
    private ParameterService parameterService;

    // ecs-获取单个
    public AliEcs getEcs(int id) {
        return aliEcsDao.getOne(id);
    }

    // ecs-查询报警
    public List<AliEcs> getAlertList() {
        Specification<AliEcs> specification = new Specification<AliEcs>() {
            @Override
            public Predicate toPredicate(Root<AliEcs> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                // 忽略数据
                String[] markValues = ignoreService.getMarkedValues("AliEcs");
                if (markValues.length > 0) {
                    for (String s : markValues) {
                        Predicate predicate = cb.notEqual(root.get("instanceId").as(String.class), s);
                        predicates.add(predicate);
                    }
                }
                // 过期数据
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, Integer.parseInt(parameterService.getValue("ALI_ECS_EXPIRED_DAY")));
                Date thresholdDate = calendar.getTime();
                predicates.add(cb.equal(root.get("status").as(String.class), "Running"));
                predicates.add(cb.lessThanOrEqualTo(root.get("expiredTime"), thresholdDate));
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        return aliEcsDao.findAll(specification);
    }

    // ecs-查询所有实例的详细信息&分页
    public Page<AliEcs> getList(Map<String, String[]> params) throws Exception {
        int pageNumber = params.get("page") == null ? 1 : Integer.parseInt(params.get("page")[0]);
        int pageSize = params.get("limit") == null ? 10 : Integer.parseInt(params.get("limit")[0]);
        AliEcs aliEcs = new AliEcs();
        aliEcs.setStatus(params.get("status") == null ? null : params.get("status")[0]);
        aliEcs.setLockReason(params.get("lockReason") == null ? null : params.get("lockReason")[0]);
        // 排除忽略数据
        String[] markValues = ignoreService.getMarkedValues("AliEcs");
        // 处理过期数据
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, Integer.parseInt(parameterService.getValue("ALI_ECS_EXPIRED_DAY")));
        Date thresholdDate = calendar.getTime();
        // 处理查询条件
        Specification<AliEcs> specification = new Specification<AliEcs>() {
            @Override
            public Predicate toPredicate(Root<AliEcs> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                // filter批量过滤
                if (params.get("key") != null && !params.get("key")[0].equals("")) {
                    String filter = "%" + params.get("key")[0] + "%";
                    Field[] fields = aliEcs.getClass().getDeclaredFields();
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
                    if (params.get("ifMarked") != null && params.get("ifMarked")[0].equals("true")) {
                        // 多个or条件
                        List<Predicate> psOr = new ArrayList<>();
                        for (String s : markValues) {
                            psOr.add(cb.like(root.get("instanceId").as(String.class), s));
                        }
                        predicates.add(cb.or(psOr.toArray(new Predicate[psOr.size()])));
                    } else {
                        for (String s : markValues) {
                            Predicate predicate = cb.notEqual(root.get("instanceId").as(String.class), s);
                            predicates.add(predicate);
                        }
                    }
                }
                // 过期数据
                if (params.get("ifExpired") != null && params.get("ifExpired")[0].equals("true")) {
                    predicates.add(cb.equal(root.get("status").as(String.class), "Running"));
                    predicates.add(cb.lessThanOrEqualTo(root.get("expiredTime"), thresholdDate));
                    cb.asc(root.get("expiredTime"));
                }
                if (StringUtils.isNoneBlank(aliEcs.getStatus())) {
                    Predicate predicate = cb.equal(root.get("status").as(String.class), aliEcs.getStatus());
                    predicates.add(predicate);
                }
                if (StringUtils.isNoneBlank(aliEcs.getLockReason())) {
                    Predicate predicate = cb.equal(root.get("lockReason").as(String.class), aliEcs.getLockReason());
                    predicates.add(predicate);
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<AliEcs> page = aliEcsDao.findAll(specification, pageable);
        // 查询后处理
        for (AliEcs ae : page.getContent()) {
            // 执行中&更新状态
            if (ae.getStatus().equals("Starting") || ae.getStatus().equals("Stopping")) {
                ae.setStatus(this.updateEcsStatus(ae).getStatus());
            }
            // 过期
            if (ae.getStatus().equals("Running")) {
                ae.setAlertExpired(ae.getExpiredTime().before(thresholdDate));
            }
            // 弃用
            if (ArrayUtils.hasString(markValues, ae.getInstanceId())) {
                ae.setAlertMarked(true);
            }
            ae.setUserName(aliService.getUserName(ae.getAccessKeyId()));
        }
        return page;
    }

    // ecs-最新状态
    public AliEcs updateEcsStatus(AliEcs aliEcs) throws Exception {
        Map<String, String> keys = aliService.getKey(aliEcs);
        IAcsClient client = aliService.getAliClient(aliEcs.getRegionId(), keys.get("id"), keys.get("secret"));
        DescribeInstancesRequest request = new DescribeInstancesRequest();
        request.setRegionId(aliEcs.getRegionId());
        request.setInstanceIds("[\"" + aliEcs.getInstanceId() + "\"]");
        DescribeInstancesResponse response = client.getAcsResponse(request);
        DescribeInstancesResponse.Instance instance = response.getInstances().get(0);
        aliEcs.setStatus(instance.getStatus());
        return aliEcs;
    }

    // ecs-预付费
    public AliEcs perPay(String instanceId, String periodUnit, int period) throws Exception {
        AliEcs aliEcs = aliEcsDao.getByInstanceId(instanceId);
        Map<String, String> keys = aliService.getKey(aliEcs);
        IAcsClient client = aliService.getAliClient("", keys.get("id"), keys.get("secret"));
        RenewInstanceRequest request = new RenewInstanceRequest();
        request.setInstanceId(instanceId);
        request.setPeriodUnit(periodUnit);
        request.setPeriod(period);
        RenewInstanceResponse response = client.getAcsResponse(request);
        // 更新数据库ecs
        return this.updateEcs(instanceId);
    }

    // ecs-更新单个ecs数据
    public AliEcs updateEcs(String instanceId) throws Exception {
        AliEcs aliEcs = aliEcsDao.getByInstanceId(instanceId);
        Map<String, String> keys = aliService.getKey(aliEcs);
        IAcsClient client = aliService.getAliClient(aliEcs.getRegionId(), keys.get("id"), keys.get("secret"));
        DescribeInstancesRequest request = new DescribeInstancesRequest();
        request.setInstanceIds("[\"" + aliEcs.getInstanceId() + "\"]");
        DescribeInstancesResponse response = client.getAcsResponse(request);
        // aeList
        List<DescribeInstancesResponse.Instance> list = response.getInstances();
        if (list.size() == 0) {
            throw new Exception("无该服务器");
        }
        aliEcs.updateData(list.get(0));
        aliEcsDao.saveAndFlush(aliEcs);
        return aliEcs;
    }


    // ecs-操作
    public void actionEcs(String action, int id) throws Exception {
        AliEcs aliEcs = aliEcsDao.getOne(id);
        Map<String, String> keys = aliService.getKey(aliEcs);
        IAcsClient client = aliService.getAliClient(aliEcs.getRegionId(), keys.get("id"), keys.get("secret"));
        switch (action) {
            case "run":
                this.startEcs(client, aliEcs.getInstanceId());
                aliEcs.setStatus("Starting");
                aliEcsDao.saveAndFlush(aliEcs);
                break;
            case "stop":
                this.stopEcs(client, aliEcs.getInstanceId(), false);
                aliEcs.setStatus("Stopping");
                aliEcsDao.saveAndFlush(aliEcs);
                break;
            case "stop-force":
                this.stopEcs(client, aliEcs.getInstanceId(), true);
                aliEcs.setStatus("Stopping");
                aliEcsDao.saveAndFlush(aliEcs);
                break;
            case "rerun":
                this.restartEcs(client, aliEcs.getInstanceId(), false);
                aliEcs.setStatus("Starting");
                aliEcsDao.saveAndFlush(aliEcs);
                break;
            case "rerun-force":
                this.restartEcs(client, aliEcs.getInstanceId(), true);
                aliEcs.setStatus("Starting");
                aliEcsDao.saveAndFlush(aliEcs);
                break;
            case "free":
                this.deleteEcs(client, aliEcs.getInstanceId(), false);
                break;
            case "free-force":
                this.deleteEcs(client, aliEcs.getInstanceId(), true);
                break;
        }
    }

    // ecs-启动
    private void startEcs(IAcsClient client, String instanceId) throws Exception {
        StartInstanceRequest request = new StartInstanceRequest();
        request.setInstanceId(instanceId);
        client.getAcsResponse(request);
    }

    // ecs-停止
    private void stopEcs(IAcsClient client, String instanceId, boolean forceStop) throws Exception {
        StopInstanceRequest request = new StopInstanceRequest();
        request.setInstanceId(instanceId);
        request.setForceStop(forceStop);
        client.getAcsResponse(request);
    }

    // ecs-重启
    private void restartEcs(IAcsClient client, String instanceId, boolean forceStop) throws Exception {
        RebootInstanceRequest request = new RebootInstanceRequest();
        request.setInstanceId(instanceId);
        request.setForceStop(forceStop);
        client.getAcsResponse(request);
    }

    // ecs-释放
    private void deleteEcs(IAcsClient client, String instanceId, boolean force) throws Exception {
        DeleteInstanceRequest request = new DeleteInstanceRequest();
        request.setInstanceId(instanceId);
        request.setForce(force);
        client.getAcsResponse(request);
    }

}
