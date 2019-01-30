package com.ywxt.Service.Aws.Impl;

import com.ywxt.Dao.Aws.Impl.AwsAccountDaoImpl;
import com.ywxt.Domain.Aws.AwsAccount;
import com.ywxt.Handler.AsyncHandler;
import com.ywxt.Utils.AsyncUtils;

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
                        // todo 处理ec2数据同步
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
                    // todo 处理ec2数据同步
                }
            };
            AsyncUtils.asyncWork(handler);
        }
        return new AwsAccountDaoImpl().deleteAccount(awsAccountId);
    }

    // todo 校验密钥
    public boolean checkAccount(String accessKeyId, String accessKeySecret) throws Exception {
        return true;
    }
}
