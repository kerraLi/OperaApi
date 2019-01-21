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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        HashMap<String, Object> resultParams = new HashMap<String, Object>();
        // normal invalid
        HashMap<String, Object> params = new HashMap<String, Object>();
        for (Object[] os : new AliCdnDaoImpl().getCountGroup(params)) {
            if (os[0].equals("online")) {
                resultParams.put("ali-cdn-" + os[1] + "-normal", os[2]);
            } else {
                resultParams.put("ali-cdn-" + os[1] + "-invalid", os[2]);
            }
        }
        // deprecated
        params = new HashMap<String, Object>();
        params.put("ifMarked", "true");
        for (Object[] os : this.getCdnTotalByAccount(params)) {
            resultParams.put("ali-cdn-" + os[0] + "-deprecated", os[1]);
        }
        return resultParams;
    }

    // CDN-获取个数按account分组
    public List<Object[]> getCdnTotalByAccount(HashMap<String, Object> params) throws Exception {
        // 是否弃用标记
        String coulmn = new ParameterIgnoreServiceImpl().getMarkKey(AliCdn.class);
        String[] markeValues = new ParameterIgnoreServiceImpl().getMarkedValues(AliCdn.class);
        HashMap<String, Object> filterParams = this.filterParamMarked(params, coulmn, markeValues);
        return new AliCdnDaoImpl().getCdnTotalByAccount(filterParams);
    }

    // CDN-获取个数
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

    // CDN-刷新&预热
    public Map<String, String> refreshCdn(String operateType, String refreshType, String objectPath) throws Exception {
        String regex = "[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+\\.?";
        Pattern p = Pattern.compile(regex);
        String[] objectPaths = objectPath.split("\\n");
        // 默认未同一个账号内，若为不同账号更新失败
        Matcher matcher = p.matcher(objectPaths[0]);
        if (matcher.find()) {
            String domain = matcher.group();
            if (this.accessKeyId == null) {
                AliCdn c = new AliCdnDaoImpl().getCdn(domain);
                System.out.println(c.getId());
                this.accessKeyId = c.getAccessKeyId();
                this.accessKeySecret = this.getAccessKeySecret(this.accessKeyId);
            }
        }
        if (operateType.equals("refresh")) {
            // 刷新
            String objectType = "File";
            if (refreshType.equals("directory")) {
                objectType = "Directory";
            }
            return this.refreshCdnObjectCaches(objectPath, objectType);
        } else if (operateType.equals("warm")) {
            // 预热
            return this.pushObjectCache(objectPath);
        } else {
            throw new Exception("error OperateType");
        }
    }

    // CDN-刷新
    private Map<String, String> refreshCdnObjectCaches(String objectPath, String objectType) throws Exception {
        IClientProfile profile = DefaultProfile.getProfile(AliRegion.QINGDAO.getRegion(), this.accessKeyId, this.accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        RefreshObjectCachesRequest request = new RefreshObjectCachesRequest();
        // 设置刷新域名多个URL使用换行符分隔"\n"或者"\r\n"
        request.setObjectPath(objectPath);
        // 可选，刷新类型值为"File"或"Directory"；默认为"File"
        request.setObjectType(objectType);
        RefreshObjectCachesResponse response = client.getAcsResponse(request);
        Map<String, String> map = new HashMap<>();
        map.put("taskId", response.getRefreshTaskId());
        map.put("requestId", response.getRequestId());
        return map;
    }

    // CDN-预热
    private Map<String, String> pushObjectCache(String objectPath) throws Exception {
        IClientProfile profile = DefaultProfile.getProfile(AliRegion.QINGDAO.getRegion(), this.accessKeyId, this.accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        PushObjectCacheRequest request = new PushObjectCacheRequest();
        // 设置刷新域名多个URL使用换行符分隔"\n"或者"\r\n"
        request.setObjectPath(objectPath);
        PushObjectCacheResponse response = client.getAcsResponse(request);
        Map<String, String> map = new HashMap<>();
        map.put("taskId", response.getPushTaskId());
        map.put("requestId", response.getRequestId());
        return map;
    }

    // CDN-刷新预热任务列表
    public List<DescribeRefreshTasksResponse.CDNTask> getCdnRefreshTask(String taskId) throws Exception {
        IClientProfile profile = DefaultProfile.getProfile(AliRegion.QINGDAO.getRegion(), this.accessKeyId, this.accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        DescribeRefreshTasksRequest request = new DescribeRefreshTasksRequest();
        request.setTaskId(taskId);
        DescribeRefreshTasksResponse response = client.getAcsResponse(request);
        return response.getTasks();
    }

}
