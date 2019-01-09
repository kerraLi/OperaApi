package com.ywxt.Service.Ali.Impl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.bssopenapi.model.v20171214.QueryAccountBalanceRequest;
import com.aliyuncs.bssopenapi.model.v20171214.QueryAccountBalanceResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.ywxt.Dao.Ali.Impl.AliAccountDaoImpl;
import com.ywxt.Dao.Ali.Impl.AliCdnDaoImpl;
import com.ywxt.Dao.Ali.Impl.AliEcsDaoImpl;
import com.ywxt.Domain.Ali.AliAccount;
import com.ywxt.Handler.AsyncHandler;
import com.ywxt.Service.Ali.AliAccountService;
import com.ywxt.Utils.AsyncUtils;
import com.ywxt.Utils.Parameter;

import java.text.DecimalFormat;
import java.util.List;

public class AliAccountServiceImpl implements AliAccountService {


    // 列表
    public List<AliAccount> getList() {
        return new AliAccountDaoImpl().getAliAccounts();
    }

    // 列表&校验金额
    public List<AliAccount> getList(boolean checkMoney) throws Exception {
        List<AliAccount> list = new AliAccountDaoImpl().getAliAccounts();
        if (checkMoney) {
            for (AliAccount aa : list) {
                if (aa.getStatus().equals("normal")) {
                    QueryAccountBalanceResponse.Data data = new AliServiceImpl(aa.getAccessKeyId(), aa.getAccessKeySecret()).getAccountBalance();
                    aa.setBalanceData(data);
                    // ali 金额 带千分符(,)
                    if (new DecimalFormat().parse(data.getAvailableAmount()).doubleValue() <= Double.parseDouble(Parameter.alertThresholds.get("ALI_ACCOUNT_BALANCE"))) {
                        aa.setAlertBalance(true);
                    }
                }
            }
        }
        return list;
    }

    // 新增/修改
    public int saveAliAccount(AliAccount aliAccount) throws Exception {
        // check ali key
        if (this.checkAccount(aliAccount.getAccessKeyId(), aliAccount.getAccessKeySecret())) {
            aliAccount.setStatus("normal");
            // update Data & 异步
            AsyncHandler handler = new AsyncHandler() {
                @Override
                public void handle() {
                    try {
                        new AliServiceImpl(aliAccount.getAccessKeyId(), aliAccount.getAccessKeySecret()).freshSourceData();
                    } catch (Exception e) {
                        // 异步处理数据错误
                        System.out.println(e.getMessage());
                    }
                }
            };
            AsyncUtils.asyncWork(handler);
        } else {
            aliAccount.setStatus("invalid");
        }
        return new AliAccountDaoImpl().saveAliAccount(aliAccount);
    }

    // 删除账号
    public boolean deleteAccount(int aliAccountId) {
        // update ali Data
        AliAccount aliAccount = new AliAccountDaoImpl().getAliAccount(aliAccountId);
        if (aliAccount.getStatus().equals("normal")) {
            // update Data & 异步
            AsyncHandler handler = new AsyncHandler() {
                @Override
                public void handle() {
                    // 删除ecs
                    new AliEcsDaoImpl().deleteAliEcsByAccessId(aliAccount.getAccessKeyId());
                    // 删除cdn
                    new AliCdnDaoImpl().deleteAliCdnByAccessId(aliAccount.getAccessKeyId());
                }
            };
            AsyncUtils.asyncWork(handler);
        }
        return new AliAccountDaoImpl().deleteAliAccount(aliAccountId);
    }

    // 校验密钥
    public boolean checkAccount(String accessKeyId, String accessKeySecret) throws Exception {
        IClientProfile profile = DefaultProfile.getProfile("", accessKeyId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        try {
            QueryAccountBalanceRequest request = new QueryAccountBalanceRequest();
            request.setEndpoint("business.aliyuncs.com");
            QueryAccountBalanceResponse response = client.getAcsResponse(request);
        } catch (ClientException e) {
            // accessKeyId 错误
            if (e.getErrCode().equals("InvalidAccessKeyId.NotFound")) {
                return false;
            }
            // accessKeySecret 错误
            if (e.getErrCode().equals("SignatureDoesNotMatch")) {
                return false;
            }
        }
        return true;
    }
}