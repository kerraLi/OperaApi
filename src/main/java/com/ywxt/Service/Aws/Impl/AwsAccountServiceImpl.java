package com.ywxt.Service.Aws.Impl;

import com.ywxt.Dao.Aws.AwsAccountDao;
import com.ywxt.Domain.Aws.AwsAccount;
import com.ywxt.Handler.AsyncHandler;
import com.ywxt.Service.Aws.AwsAccountService;
import com.ywxt.Service.Aws.AwsService;
import com.ywxt.Utils.AsyncUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.services.iam.model.IamException;
import software.amazon.awssdk.services.iam.model.ListUsersRequest;
import software.amazon.awssdk.services.iam.model.ListUsersResponse;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.iam.IamClient;

import java.util.List;

@Service
public class AwsAccountServiceImpl implements AwsAccountService {

    @Autowired
    private AwsAccountDao awsAccountDao;
    @Autowired
    private AwsService awsService;

    // 列表
    public List<AwsAccount> getList() {
        return awsAccountDao.findAll();
    }

    // 新增/修改
    public AwsAccount saveAccount(AwsAccount awsAccount) {
        // check key
        if (this.checkAccount(awsAccount.getAccessKeyId(), awsAccount.getAccessKeySecret())) {
            awsAccount.setStatus("normal");
            // update Data & 异步
            AsyncHandler handler = new AsyncHandler() {
                @Override
                public void handle() {
                    try {
                        try {
                            awsService.freshSourceData(awsAccount.getAccessKeyId(), awsAccount.getAccessKeySecret());
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
        return awsAccountDao.saveAndFlush(awsAccount);
    }

    // 删除账号
    public void deleteAccount(int id) {
        // update Data
        AwsAccount awsAccount = awsAccountDao.findAwsAccountById(id);
        if (awsAccount.getStatus().equals("normal")) {
            // update Data & 异步
            AsyncHandler handler = new AsyncHandler() {
                @Override
                public void handle() {
                    awsService.removeSourceData(awsAccount.getAccessKeyId());
                }
            };
            AsyncUtils.asyncWork(handler);
        }
        awsAccountDao.deleteById(id);
    }

    // 校验密钥
    private boolean checkAccount(String accessKeyId, String accessKeySecret) {
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
