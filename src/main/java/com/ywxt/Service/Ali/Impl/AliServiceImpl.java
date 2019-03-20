package com.ywxt.Service.Ali.Impl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.cdn.model.v20141111.*;
import com.aliyuncs.ecs.model.v20140526.*;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.scdn.model.v20171115.DescribeScdnUserDomainsRequest;
import com.aliyuncs.scdn.model.v20171115.DescribeScdnUserDomainsResponse;
import com.ywxt.Dao.Ali.AliAccountDao;
import com.ywxt.Dao.Ali.AliCdnDao;
import com.ywxt.Dao.Ali.AliEcsDao;
import com.ywxt.Dao.Ali.AliScdnDao;
import com.ywxt.Domain.Ali.AliAccount;
import com.ywxt.Domain.Ali.AliCdn;
import com.ywxt.Domain.Ali.AliEcs;
import com.ywxt.Domain.Ali.AliScdn;
import com.ywxt.Domain.Log.LogRefresh;
import com.ywxt.Enum.AliRegion;
import com.ywxt.Service.Ali.AliService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ywxt.Service.System.Impl.RefreshServiceImpl;

import java.util.*;

@Service("aliService")
public class AliServiceImpl implements AliService {

    @Autowired
    private AliAccountDao aliAccountDao;
    @Autowired
    private AliEcsDao aliEcsDao;
    @Autowired
    private AliCdnDao aliCdnDao;
    @Autowired
    private AliScdnDao aliScdnDao;

    private HashMap<String, String> userNameMap = new HashMap<>();

    //获取阿里连接
    public IAcsClient getAliClient(String regionId, AliAccount aliAccount) {
        IClientProfile profile = DefaultProfile.getProfile(regionId,
                aliAccount.getAccessKeyId(), aliAccount.getAccessKeySecret());
        return new DefaultAcsClient(profile);
    }

    //获取阿里连接
    public IAcsClient getAliClient(String regionId, String keyId, String keySecret) {
        IClientProfile profile = DefaultProfile.getProfile(regionId,
                keyId, keySecret);
        return new DefaultAcsClient(profile);
    }

    // 获取ali-key
    public Map<String, String> getKey(AliCdn aliCdn) throws Exception {
        Map<String, String> keyMap = new HashMap<String, String>();
        keyMap.put("id", aliCdn.getAccessKeyId());
        keyMap.put("secret", this.getAccessKeySecret(aliCdn.getAccessKeyId()));
        return keyMap;
    }

    // 获取ali-key
    public Map<String, String> getKey(AliEcs aliEcs) throws Exception {
        Map<String, String> keyMap = new HashMap<String, String>();
        keyMap.put("id", aliEcs.getAccessKeyId());
        keyMap.put("secret", this.getAccessKeySecret(aliEcs.getAccessKeyId()));
        return keyMap;
    }

    // todo 查询可用区
    public void getRegion(String keyId, String keySecret) throws Exception {
        IClientProfile profile = DefaultProfile.getProfile("", keyId, keySecret);
        IAcsClient client = new DefaultAcsClient(profile);
    }

    // 删除ali源数据
    public void removeSourceData(String keyId) {
        aliEcsDao.deleteByAccessKeyId(keyId);
        aliCdnDao.deleteByAccessKeyId(keyId);
        aliScdnDao.deleteByAccessKeyId(keyId);
    }

    // 更新ali源数据
    public void freshSourceData(String keyId, String keySecret) throws Exception {
        // 记录更新时间
        LogRefresh log = new LogRefresh();
        log.setTime(new Date());
        log.setType("ali");
        new RefreshServiceImpl().saveRefreshLog(log);
        // 更新ecs数据 && 更新cdn域名数据
        this.freshEcsData(keyId, keySecret);
        this.freshCdnData(keyId, keySecret);
        this.freshScdnData(keyId, keySecret);
    }

    //更新scdn数据
    private void freshScdnData(String keyId, String keySecret) throws Exception {
        List<AliScdn> list = aliScdnDao.findByAccessKeyId(keyId);
        if (CollectionUtils.isNotEmpty(list)) {
            aliScdnDao.deleteInBatch(list);
        }
        AliAccount aliAccount = aliAccountDao.getByAccessKeyId(keyId);
        List<AliScdn> aliScdns = new ArrayList<>();
        IClientProfile profile = DefaultProfile.getProfile("", keyId, keySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        int pageSize = 20;
        int pageNumber = 1;
        for (; ; ) {
            DescribeScdnUserDomainsRequest scdnRequest = new DescribeScdnUserDomainsRequest();
            scdnRequest.setPageSize(pageSize);
            scdnRequest.setPageNumber(pageNumber);
            scdnRequest.setEndpoint("scdn.aliyuncs.com");
            DescribeScdnUserDomainsResponse response = null;
            try {
                response = client.getAcsResponse(scdnRequest);
            } catch (Exception e) {
                //账号没有开启scdn报的错误
            }
            if (response != null) {
                List<DescribeScdnUserDomainsResponse.PageData> datas = response.getDomains();
                for (DescribeScdnUserDomainsResponse.PageData data : datas) {
                    AliScdn aliScdn = new AliScdn(aliAccount, data);
                    aliScdns.add(aliScdn);
                }
                // 最后一页 跳出
                if (datas.size() < pageSize) {
                    break;
                }
                pageNumber++;
            }
            break;
        }
        if (CollectionUtils.isNotEmpty(aliScdns)) {
            aliScdnDao.saveAll(aliScdns);
            aliScdnDao.flush();
        }
    }

    // 更新ecs数据
    private void freshEcsData(String keyId, String keySecret) throws Exception {
        aliEcsDao.deleteByAccessKeyId(keyId);
        List<AliEcs> aeList = new ArrayList<>();
        for (AliRegion e : AliRegion.values()) {
            IClientProfile profile = DefaultProfile.getProfile(e.getRegion(), keyId, keySecret);
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
                    AliEcs ecs = new AliEcs(keyId, i);
                    aeList.add(ecs);
                }
                // 最后一页 跳出
                if (describeInstancesResponse.getInstances().size() < pageSize) {
                    break;
                }
                pageNumber++;
            }
        }
        aliEcsDao.saveAll(aeList);
        aliEcsDao.flush();
    }

    // 更新cdn数据
    public void freshCdnData(String keyId, String keySecret) throws Exception {
        aliCdnDao.deleteByAccessKeyId(keyId);
        List<AliCdn> acList = new ArrayList<>();
        // cdn不分地区：只查一个地区
        IClientProfile profile = DefaultProfile.getProfile(AliRegion.QINGDAO.getRegion(), keyId, keySecret);
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
                AliCdn cdnDomain = new AliCdn(keyId, domain);
                acList.add(cdnDomain);
            }
            // 最后一页 跳出
            if (describeUserDomainsResponse.getDomains().size() < pageSize) {
                break;
            }
            pageNumber++;
        }
        aliCdnDao.saveAll(acList);
        aliCdnDao.flush();
    }

    // todo 过滤弃用param
    public HashMap<String, Object> filterParamMarked(HashMap<String, Object> params, String coulmn, String[] markeValues) {
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
    public String getUserName(String keyId) {
        if (this.userNameMap.get(keyId) == null) {
            AliAccount aliAccount = aliAccountDao.getByAccessKeyId(keyId);
            this.userNameMap.put(keyId, aliAccount.getUserName());
            return aliAccount.getUserName();
        } else {
            return this.userNameMap.get(keyId);
        }
    }

    // 获取accessKeySecret
    public String getAccessKeySecret(String accessKeyId) {
        AliAccount aliAccount = aliAccountDao.getByAccessKeyId(accessKeyId);
        return aliAccount.getAccessKeySecret();
    }

}
