package com.ywxt.Service.Ali.Impl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.bssopenapi.model.v20171214.QueryAccountBalanceRequest;
import com.aliyuncs.bssopenapi.model.v20171214.QueryAccountBalanceResponse;
import com.aliyuncs.cdn.model.v20141111.*;
import com.aliyuncs.ecs.model.v20140526.*;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.ywxt.Dao.Ali.Impl.AliAccountDaoImpl;
import com.ywxt.Dao.Ali.Impl.AliCdnDaoImpl;
import com.ywxt.Dao.Ali.Impl.AliEcsDaoImpl;
import com.ywxt.Domain.Ali.AliAccount;
import com.ywxt.Domain.Ali.AliCdn;
import com.ywxt.Domain.Ali.AliEcs;
import com.ywxt.Domain.Log.LogRefresh;
import com.ywxt.Enum.AliRegion;
import com.ywxt.Service.Ali.AliService;
import com.ywxt.Service.System.Impl.RefreshServiceImpl;

import java.util.*;

public class AliServiceImpl implements AliService {

    private String accessKeyId;
    private String accessKeySecret;
    private HashMap<String, String> userNameMap = new HashMap<>();

    public AliServiceImpl() {
    }

    public AliServiceImpl(String accessKeyId) throws Exception {
        AliAccount aliAccount = new AliAccountDaoImpl().getAliAccount(accessKeyId);
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = aliAccount.getAccessKeySecret();
    }

    public AliServiceImpl(String keyId, String keySecret) {
        this.accessKeyId = keyId;
        this.accessKeySecret = keySecret;
    }

    // 账户
    public QueryAccountBalanceResponse.Data getAccountBalance() throws Exception {
        IClientProfile profile = DefaultProfile.getProfile("", this.accessKeyId, this.accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        QueryAccountBalanceRequest request = new QueryAccountBalanceRequest();
        request.setEndpoint("business.aliyuncs.com");
        QueryAccountBalanceResponse response = client.getAcsResponse(request);
        return response.getData();
    }

    // 查询可用区
    // todo
    public void getRegion() throws Exception {
        IClientProfile profile = DefaultProfile.getProfile("", this.accessKeyId, this.accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
    }

    // 更新ali源数据(按账户更新)
    public void freshSourceData() throws Exception {
        // 记录更新时间
        LogRefresh log = new LogRefresh();
        log.setTime(new Date());
        log.setType("ali");
        new RefreshServiceImpl().saveRefreshLog(log);
        // 更新ecs数据 && 更新cdn域名数据
        this.freshEcsData();
        this.freshCdnData();
    }

    // 更新ecs数据
    public void freshEcsData() throws Exception {
        new AliEcsDaoImpl().deleteAliEcsByAccessId(this.accessKeyId);
        List<AliEcs> aeList = new ArrayList<>();
        for (AliRegion e : AliRegion.values()) {
            IClientProfile profile = DefaultProfile.getProfile(e.getRegion(), this.accessKeyId, this.accessKeySecret);
            IAcsClient client = new DefaultAcsClient(profile);
            int pageSize = 20;
            // ecs
            int pageNumber = 1;
            while (true) {
                DescribeInstancesRequest describeInstancesRequest = new DescribeInstancesRequest();
                describeInstancesRequest.setPageSize(pageSize);
                describeInstancesRequest.setPageNumber(pageNumber);
                DescribeInstancesResponse describeInstancesResponse = client.getAcsResponse(describeInstancesRequest);
                // aeList
                for (DescribeInstancesResponse.Instance i : describeInstancesResponse.getInstances()) {
                    AliEcs ecs = new AliEcs(this.accessKeyId, i);
                    aeList.add(ecs);
                }
                // 最后一页 跳出
                if (describeInstancesResponse.getInstances().size() < pageSize) {
                    break;
                }
                pageNumber++;
            }
        }
        new AliEcsDaoImpl().saveAliEcses(aeList);
    }

    // 更新cdn数据
    public void freshCdnData() throws Exception {
        new AliCdnDaoImpl().deleteAliCdnByAccessId(this.accessKeyId);
        List<AliCdn> acList = new ArrayList<>();
        // cdn不分地区：只查一个地区
        IClientProfile profile = DefaultProfile.getProfile(AliRegion.QINGDAO.getRegion(), this.accessKeyId, this.accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        int pageSize = 20;
        int pageNumber = 1;
        while (true) {
            DescribeUserDomainsRequest describeUserDomainsRequest = new DescribeUserDomainsRequest();
            describeUserDomainsRequest.setPageSize(pageSize);
            describeUserDomainsRequest.setPageNumber(pageNumber);
            DescribeUserDomainsResponse describeUserDomainsResponse = client.getAcsResponse(describeUserDomainsRequest);
            // acList
            for (DescribeUserDomainsResponse.PageData domain : describeUserDomainsResponse.getDomains()) {
                AliCdn cdnDomain = new AliCdn(this.accessKeyId, domain);
                acList.add(cdnDomain);
            }
            // 最后一页 跳出
            if (describeUserDomainsResponse.getDomains().size() < pageSize) {
                break;
            }
            pageNumber++;
        }
        new AliCdnDaoImpl().saveAliCdns(acList);
    }

    // 过滤弃用param
    protected HashMap<String, Object> filterParamMarked(HashMap<String, Object> params, String coulmn, String[] markeValues) {
        boolean ifMarked = (params.get("ifMarked") != null) && (params.get("ifMarked").equals("true"));
        if (ifMarked) {
            if (markeValues.length > 0) {
                params.put(coulmn + "@in", markeValues);
            } else {
                params.put(coulmn + "@eq", "");
            }
        } else {
            if (markeValues.length > 0) {
                params.put(coulmn + "@notIn", markeValues);
            }
        }
        params.remove("ifMarked");
        return params;
    }

    // 获取userName
    public String getUserName(String accessKeyId) throws Exception {
        if (this.userNameMap.get(accessKeyId) == null) {
            AliAccount aliAccount = new AliAccountDaoImpl().getAliAccount(accessKeyId);
            this.userNameMap.put(accessKeyId, aliAccount.getUserName());
            return aliAccount.getUserName();
        } else {
            return this.userNameMap.get(accessKeyId);
        }
    }

    // 获取accessKeySecret
    public String getAccessKeySecret(String accessKeyId) throws Exception {
        AliAccount aliAccount = new AliAccountDaoImpl().getAliAccount(accessKeyId);
        return aliAccount.getAccessKeySecret();
    }

}
