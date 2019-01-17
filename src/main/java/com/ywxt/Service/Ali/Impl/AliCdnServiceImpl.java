package com.ywxt.Service.Ali.Impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.cdn.model.v20141111.*;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.ywxt.Dao.Ali.Impl.AliAccountDaoImpl;
import com.ywxt.Dao.Ali.Impl.AliCdnDaoImpl;
import com.ywxt.Domain.Ali.AliAccount;
import com.ywxt.Domain.Ali.AliCdn;
import com.ywxt.Enum.AliRegion;
import com.ywxt.Service.Ali.AliCdnService;
import com.ywxt.Service.Impl.ParameterIgnoreServiceImpl;
import com.ywxt.Utils.ArrayUtils;

import java.util.*;

public class AliCdnServiceImpl extends AliServiceImpl implements AliCdnService {


    private String accessKeyId;
    private String accessKeySecret;
    private HashMap<String, String> userNameMap = new HashMap<>();

    public AliCdnServiceImpl() {
    }

    public AliCdnServiceImpl(String accessKeyId) throws Exception {
        AliAccount aliAccount = new AliAccountDaoImpl().getAliAccount(accessKeyId);
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = aliAccount.getAccessKeySecret();
    }

    public AliCdnServiceImpl(String keyId, String keySecret) {
        this.accessKeyId = keyId;
        this.accessKeySecret = keySecret;
    }

    // 获取dash数据
    public HashMap<String, Object> getDashData() throws Exception {
        // normal invalid
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("accessKeyId",this.accessKeyId);
        List<Object[]> list = new AliCdnDaoImpl().getCountGroup(params);
        Long normal = 0L;
        Long invalid = 0L;
        for (Object[] os : list) {
            if (os[0].equals("online")) {
                normal = (Long) os[1];
            } else {
                invalid += (Long) os[1];
            }
        }
        // deprecated
        params = new HashMap<String, Object>();
        params.put("ifMarked", "true");
        params.put("accessKeyId",this.accessKeyId);
        int deprecated = this.getCdnTotal(params);
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("normal", normal);
        result.put("invalid", invalid);
        result.put("expired", 0);
        result.put("deprecated", deprecated);
        return result;
    }

    // ecs-获取个数
    public int getCdnTotal(HashMap<String, Object> params) throws Exception {
        // 是否弃用标记
        String coulmn = new ParameterIgnoreServiceImpl().getMarkKey(AliCdn.class);
        String[] markeValues = new ParameterIgnoreServiceImpl().getMarkedValues(AliCdn.class);
        HashMap<String, Object> filterParams = this.filterParamMarked(params, coulmn, markeValues);
        return new AliCdnDaoImpl().getCdnTotal(filterParams);
    }

    // CDN-获取单个
    public AliCdn getCdn(int id) {
        return new AliCdnDaoImpl().getCdn(id);
    }

    // CDN-域名列表&分页信息
    public JSONObject getCdnDomainList(HashMap<String, Object> params, int pageNumber, int pageSize) throws Exception {
        // 是否弃用标记
        String coulmn = new ParameterIgnoreServiceImpl().getMarkKey(AliCdn.class);
        String[] markeValues = new ParameterIgnoreServiceImpl().getMarkedValues(AliCdn.class);
        HashMap<String, Object> filterParams = this.filterParamMarked(params, coulmn, markeValues);
        List<AliCdn> list = new AliCdnDaoImpl().getCdnList(filterParams, pageNumber, pageSize);
        for (AliCdn ac : list) {
            if (ArrayUtils.hasString(markeValues, ac.getDomainName())) {
                ac.setAlertMarked(true);
            }
            ac.setUserName(this.getUserName(ac.getAccessKeyId()));
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", new AliCdnDaoImpl().getCdnTotal(filterParams));
        jsonObject.put("items", list);
        return jsonObject;
    }

    // CDN-刷新
    public Map<String, String> refreshCdnObjectCaches(String objectPath, String objectType) throws Exception {
        IClientProfile profile = DefaultProfile.getProfile(AliRegion.QINGDAO.getRegion(), this.accessKeyId, this.accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        RefreshObjectCachesRequest request = new RefreshObjectCachesRequest();
        // 设置刷新域名多个URL使用换行符分隔"\n"或者"\r\n"
        request.setObjectPath(objectPath);
        // 可选，刷新类型值为"File"或"Directory"；默认为"File"
        request.setObjectType(objectType);
        RefreshObjectCachesResponse response = client.getAcsResponse(request);
        Map<String, String> map = new HashMap<>();
        map.put("refreshTaskId", response.getRefreshTaskId());
        map.put("requestId", response.getRequestId());
        return map;
    }

    // CDN-刷新预热任务
    public List<DescribeRefreshTasksResponse.CDNTask> getCdnRefreshTask(String taskId) throws Exception {
        IClientProfile profile = DefaultProfile.getProfile(AliRegion.QINGDAO.getRegion(), this.accessKeyId, this.accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        DescribeRefreshTasksRequest request = new DescribeRefreshTasksRequest();
        request.setTaskId(taskId);
        DescribeRefreshTasksResponse response = client.getAcsResponse(request);
        return response.getTasks();
    }

}
