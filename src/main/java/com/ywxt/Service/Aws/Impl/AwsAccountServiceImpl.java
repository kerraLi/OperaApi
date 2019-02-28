package com.ywxt.Service.Aws.Impl;

import com.ywxt.Dao.Aws.Impl.AwsAccountDaoImpl;
import com.ywxt.Dao.Aws.Impl.AwsEc2DaoImpl;
import com.ywxt.Domain.Aws.AwsAccount;
import com.ywxt.Handler.AsyncHandler;
import com.ywxt.Utils.AsyncUtils;

import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.services.iam.model.IamException;
import software.amazon.awssdk.services.iam.model.ListUsersRequest;
import software.amazon.awssdk.services.iam.model.ListUsersResponse;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.iam.IamClient;

import java.util.HashMap;
import java.util.List;

public class AwsAccountServiceImpl {

    // 获取总数
    public int getTotal(HashMap<String, Object> params) throws Exception {
        return new AwsAccountDaoImpl().getListTotal(params);
    }

    // 列表
    public List<AwsAccount> getList() {
        return new AwsAccountDaoImpl().getAccounts();
    }

    // 新增/修改
    public int saveAccount(AwsAccount awsAccount) throws Exception {
        // check key
        if (this.checkAccount(awsAccount.getAccessKeyId(), awsAccount.getAccessKeySecret())) {
            awsAccount.setStatus("normal");
            // update Data & 异步
            AsyncHandler handler = new AsyncHandler() {
                @Override
                public void handle() {
                    try {
                        try {
                            new AwsServiceImpl(awsAccount.getAccessKeyId(), awsAccount.getAccessKeySecret()).freshSourceData();
                        } catch (Exception e) {
                            // 异步处理数据错误
                            System.out.println(e.getMessage());
                        }
                    } catch (Exception e) {
                        // 异步处理数据错误
                        System.out.println(e.getMessage());
                    }
                }
            };
            AsyncUtils.asyncWork(handler);
        } else {
            awsAccount.setStatus("invalid");
        }
        return new AwsAccountDaoImpl().saveAccount(awsAccount);
    }

    // 删除账号
    public boolean deleteAccount(int awsAccountId) {
        // update Data
        AwsAccount awsAccount = new AwsAccountDaoImpl().getAccount(awsAccountId);
        if (awsAccount.getStatus().equals("normal")) {
            // update Data & 异步
            AsyncHandler handler = new AsyncHandler() {
                @Override
                public void handle() {
                    // 删除ec2
                    new AwsEc2DaoImpl().deleteAwsEc2ByAccessId(awsAccount.getAccessKeyId());
                }
            };
            AsyncUtils.asyncWork(handler);
        }
        return new AwsAccountDaoImpl().deleteAccount(awsAccountId);
    }

    // 校验密钥
    public boolean checkAccount(String accessKeyId, String accessKeySecret) throws Exception {
        try {
            Region region = Region.AWS_GLOBAL;
            IamClient client = IamClient.builder()
                    .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKeyId, accessKeySecret)))
                    .region(region).build();
            ListUsersRequest request = ListUsersRequest.builder().build();
            ListUsersResponse response = client.listUsers(request);
        } catch (IamException iamException) {
            // 密钥错误等
            return false;
        }
        return true;
    }
}
