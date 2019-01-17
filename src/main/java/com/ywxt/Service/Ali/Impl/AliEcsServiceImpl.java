package com.ywxt.Service.Ali.Impl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.*;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.alibaba.fastjson.JSONObject;
import com.ywxt.Dao.Ali.Impl.AliAccountDaoImpl;
import com.ywxt.Dao.Ali.Impl.AliEcsDaoImpl;
import com.ywxt.Domain.Ali.AliAccount;
import com.ywxt.Domain.Ali.AliEcs;
import com.ywxt.Service.Ali.AliEcsService;
import com.ywxt.Service.Impl.ParameterIgnoreServiceImpl;
import com.ywxt.Utils.ArrayUtils;
import com.ywxt.Utils.Parameter;

import java.util.*;

public class AliEcsServiceImpl extends AliServiceImpl implements AliEcsService {

    private String accessKeyId;
    private String accessKeySecret;
    private HashMap<String, String> userNameMap = new HashMap<>();

    public AliEcsServiceImpl() {
    }

    public AliEcsServiceImpl(String accessKeyId) throws Exception {
        AliAccount aliAccount = new AliAccountDaoImpl().getAliAccount(accessKeyId);
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = aliAccount.getAccessKeySecret();
    }

    public AliEcsServiceImpl(String keyId, String keySecret) {
        this.accessKeyId = keyId;
        this.accessKeySecret = keySecret;
    }

    // 获取dash数据
    public HashMap<String, Object> getDashData() throws Exception {
        HashMap<String, Object> resultParams = new HashMap<String, Object>();
        // normal invalid
        HashMap<String, Object> params = new HashMap<String, Object>();
        for (Object[] os : new AliEcsDaoImpl().getCountGroup(params)) {
            if (os[0].equals("Running")) {
                resultParams.put(os[1] + "-normal", os[2]);
            } else {
                resultParams.put(os[1] + "-invalid", os[2]);
            }
        }
        // expired
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, Integer.parseInt(Parameter.alertThresholds.get("ALI_ECS_EXPIRED_DAY")));
        Date thresholdDate = calendar.getTime();
        params = new HashMap<String, Object>();
        params.put("status", "Running");
        params.put("expiredTime@lt", thresholdDate);
        for (Object[] os : this.getEcsTotalByAccount(params)) {
            resultParams.put(os[0] + "-expired", os[1]);
        }
        // deprecated
        params = new HashMap<String, Object>();
        params.put("ifMarked", "true");
        for (Object[] os : this.getEcsTotalByAccount(params)) {
            resultParams.put(os[0] + "-deprecated", os[1]);
        }
        return resultParams;
    }

    // ecs-获取个数按account分组
    public List<Object[]> getEcsTotalByAccount(HashMap<String, Object> params) throws Exception {
        // 是否弃用标记
        String coulmn = new ParameterIgnoreServiceImpl().getMarkKey(AliEcs.class);
        String[] markeValues = new ParameterIgnoreServiceImpl().getMarkedValues(AliEcs.class);
        HashMap<String, Object> filterParams = this.filterParamMarked(params, coulmn, markeValues);
        return new AliEcsDaoImpl().getAliEcsesTotalByAccount(filterParams);
    }

    // ecs-获取个数
    public int getEcsTotal(HashMap<String, Object> params) throws Exception {
        // 是否弃用标记
        String coulmn = new ParameterIgnoreServiceImpl().getMarkKey(AliEcs.class);
        String[] markeValues = new ParameterIgnoreServiceImpl().getMarkedValues(AliEcs.class);
        HashMap<String, Object> filterParams = this.filterParamMarked(params, coulmn, markeValues);
        return new AliEcsDaoImpl().getAliEcsesTotal(filterParams);
    }

    // ecs-获取单个
    public AliEcs getEcs(int id) {
        return new AliEcsDaoImpl().getEcs(id);
    }

    // ecs-查询所有
    public List<AliEcs> getEcsList(HashMap<String, Object> params) throws Exception {
        // 是否弃用标记
        String coulmn = new ParameterIgnoreServiceImpl().getMarkKey(AliEcs.class);
        String[] markeValues = new ParameterIgnoreServiceImpl().getMarkedValues(AliEcs.class);
        HashMap<String, Object> filterParams = this.filterParamMarked(params, coulmn, markeValues);
        List<AliEcs> list = new AliEcsDaoImpl().getAliEcsesList(filterParams);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, Integer.parseInt(Parameter.alertThresholds.get("ALI_ECS_EXPIRED_DAY")));
        Date thresholdDate = calendar.getTime();
        for (AliEcs ae : list) {
            if (ae.getStatus().equals("Running")) {
                ae.setAlertExpired(ae.getExpiredTime().before(thresholdDate));
            }
            if (ArrayUtils.hasString(markeValues, ae.getInstanceId())) {
                ae.setAlertMarked(true);
            }
            ae.setUserName(this.getUserName(ae.getAccessKeyId()));
        }
        return list;
    }

    // ecs-查询所有实例的详细信息&分页
    public JSONObject getEcsList(HashMap<String, Object> params, int pageNumber, int pageSize) throws Exception {
        // 是否弃用标记
        String coulmn = new ParameterIgnoreServiceImpl().getMarkKey(AliEcs.class);
        String[] markeValues = new ParameterIgnoreServiceImpl().getMarkedValues(AliEcs.class);
        HashMap<String, Object> filterParams = this.filterParamMarked(params, coulmn, markeValues);
        List<AliEcs> list = new AliEcsDaoImpl().getAliEcsesList(filterParams, pageNumber, pageSize);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, Integer.parseInt(Parameter.alertThresholds.get("ALI_ECS_EXPIRED_DAY")));
        Date thresholdDate = calendar.getTime();
        for (AliEcs ae : list) {
            if (ae.getStatus().equals("Running")) {
                ae.setAlertExpired(ae.getExpiredTime().before(thresholdDate));
            }
            if (ArrayUtils.hasString(markeValues, ae.getInstanceId())) {
                ae.setAlertMarked(true);
            }
            ae.setUserName(this.getUserName(ae.getAccessKeyId()));
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", new AliEcsDaoImpl().getAliEcsesTotal(filterParams));
        jsonObject.put("items", list);
        return jsonObject;
    }

    // ecs-启动
    public void startEcs(String regionId, String instanceId) throws Exception {
        IClientProfile profile = DefaultProfile.getProfile(regionId, this.accessKeyId, this.accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        StartInstanceRequest request = new StartInstanceRequest();
        request.setInstanceId(instanceId);
        client.getAcsResponse(request);
    }

    // ecs-停止
    public void stopEcs(String regionId, String instanceId, boolean forceStop) throws Exception {
        IClientProfile profile = DefaultProfile.getProfile(regionId, this.accessKeyId, this.accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        StopInstanceRequest request = new StopInstanceRequest();
        request.setInstanceId(instanceId);
        request.setForceStop(forceStop);
        client.getAcsResponse(request);
    }

    // ecs-重启
    public void restartEcs(String regionId, String instanceId, boolean forceStop) throws Exception {
        IClientProfile profile = DefaultProfile.getProfile(regionId, this.accessKeyId, this.accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        RebootInstanceRequest request = new RebootInstanceRequest();
        request.setInstanceId(instanceId);
        request.setForceStop(forceStop);
        client.getAcsResponse(request);
    }

    // ecs-释放
    public void deleteEcs(String regionId, String instanceId, boolean force) throws Exception {
        IClientProfile profile = DefaultProfile.getProfile(regionId, this.accessKeyId, this.accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        DeleteInstanceRequest request = new DeleteInstanceRequest();
        request.setInstanceId(instanceId);
        request.setForce(force);
        client.getAcsResponse(request);
    }

}
