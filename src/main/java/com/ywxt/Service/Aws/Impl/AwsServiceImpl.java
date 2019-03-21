package com.ywxt.Service.Aws.Impl;

import com.ywxt.Dao.Aws.AwsAccountDao;
import com.ywxt.Dao.Aws.AwsEc2Dao;
import com.ywxt.Domain.Aws.AwsAccount;
import com.ywxt.Domain.Aws.AwsEc2;
import com.ywxt.Domain.Log.LogRefresh;
import com.ywxt.Service.Aws.AwsService;
import com.ywxt.Service.System.Impl.RefreshServiceImpl;
import com.ywxt.Service.System.RefreshService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesRequest;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesResponse;
import software.amazon.awssdk.services.ec2.model.Instance;
import software.amazon.awssdk.services.ec2.model.Reservation;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service("awsService")
public class AwsServiceImpl implements AwsService {

    @Autowired
    private AwsAccountDao awsAccountDao;
    @Autowired
    private AwsEc2Dao awsEc2Dao;
    @Autowired
    private RefreshService refreshService;

    private HashMap<String, String> userNameMap = new HashMap<>();

    // 更新源数据
    public void freshSourceData(String keyId, String keySecret) throws Exception {
        // 记录更新时间
        LogRefresh log = new LogRefresh();
        log.setTime(new Date());
        log.setType("aws");
        refreshService.saveRefreshLog(log);
        // 刷新
        this.freshEc2(keyId, keySecret);
    }

    // 删除源数据
    public void removeSourceData(String keyId) {
        awsEc2Dao.deleteByAccessKeyId(keyId);
    }

    // 更新ec2
    public void freshEc2(String keyId, String keySecret) throws Exception {
        awsEc2Dao.deleteByAccessKeyId(keyId);
        List<AwsEc2> aeList = new ArrayList<>();
        for (Region region : Region.regions()) {
            // 排除不可用区
            if (!region.isGlobalRegion()
                    && !region.id().equals("us-gov-east-1")
                    && !region.id().equals("cn-north-1")
                    && !region.id().equals("us-gov-west-1")
                    && !region.id().equals("cn-northwest-1")
            ) {
                Ec2Client client = Ec2Client.builder()
                        .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(keyId, keySecret)))
                        .region(region).build();
                DescribeInstancesRequest request = DescribeInstancesRequest.builder().build();
                boolean done = false;
                while (!done) {
                    DescribeInstancesResponse response = client.describeInstances(request);
                    for (Reservation reservation : response.reservations()) {
                        for (Instance instance : reservation.instances()) {
                            AwsEc2 awsEc2 = new AwsEc2(keyId, region.id(), instance);
                            aeList.add(awsEc2);
                        }
                    }
                    if (response.nextToken() == null) {
                        done = true;
                    }
                }
            }
        }
        awsEc2Dao.saveAll(aeList);
        awsEc2Dao.flush();
    }

    // 获取userName
    public String getUserName(String accessKeyId) {
        if (this.userNameMap.get(accessKeyId) == null) {
            AwsAccount awsAccount = awsAccountDao.findAwsAccountByAccessKeyId(accessKeyId);
            this.userNameMap.put(accessKeyId, awsAccount.getUserName());
            return awsAccount.getUserName();
        } else {
            return this.userNameMap.get(accessKeyId);
        }
    }

}
