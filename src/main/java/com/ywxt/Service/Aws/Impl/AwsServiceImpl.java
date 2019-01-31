package com.ywxt.Service.Aws.Impl;

import com.ywxt.Dao.Aws.Impl.AwsAccountDaoImpl;
import com.ywxt.Dao.Aws.Impl.AwsEc2DaoImpl;
import com.ywxt.Domain.Aws.AwsAccount;
import com.ywxt.Domain.Aws.AwsEc2;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesRequest;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesResponse;
import software.amazon.awssdk.services.ec2.model.Instance;
import software.amazon.awssdk.services.ec2.model.Reservation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AwsServiceImpl {

    private String accessKeyId;
    private String accessKeySecret;
    private HashMap<String, String> userNameMap = new HashMap<>();


    public AwsServiceImpl() {

    }

    public AwsServiceImpl(String accessKeyId) throws Exception {
        AwsAccount awsAccount = new AwsAccountDaoImpl().getAccount(accessKeyId);
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = awsAccount.getAccessKeySecret();
    }

    public AwsServiceImpl(String keyId, String keySecret) {
        this.accessKeyId = keyId;
        this.accessKeySecret = keySecret;
    }

    // 更新源数据
    public void freshSourceData() throws Exception {
        this.freshEc2();
    }

    // 更新ec2
    public void freshEc2() throws Exception {
        new AwsEc2DaoImpl().deleteAwsEc2ByAccessId(this.accessKeyId);
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
                        .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKeyId, accessKeySecret)))
                        .region(region).build();
                DescribeInstancesRequest request = DescribeInstancesRequest.builder().build();
                boolean done = false;
                while (!done) {
                    DescribeInstancesResponse response = client.describeInstances(request);
                    for (Reservation reservation : response.reservations()) {
                        for (Instance instance : reservation.instances()) {
                            AwsEc2 awsEc2 = new AwsEc2(this.accessKeyId, region.id(), instance);
                            aeList.add(awsEc2);
                        }
                    }
                    if (response.nextToken() == null) {
                        done = true;
                    }
                }
            }
        }
        for (AwsEc2 awsEc2 : aeList) {
            System.out.println(awsEc2.getRegion() + "=========" + awsEc2.getInstanceId());
        }
        new AwsEc2DaoImpl().saveAwsEc2s(aeList);
    }

    // 获取userName
    public String getUserName(String accessKeyId) throws Exception {
        if (this.userNameMap.get(accessKeyId) == null) {
            AwsAccount awsAccount = new AwsAccountDaoImpl().getAccount(accessKeyId);
            this.userNameMap.put(accessKeyId, awsAccount.getUserName());
            return awsAccount.getUserName();
        } else {
            return this.userNameMap.get(accessKeyId);
        }
    }

}
