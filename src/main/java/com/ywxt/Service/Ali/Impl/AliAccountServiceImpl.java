package com.ywxt.Service.Ali.Impl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.bssopenapi.model.v20171214.QueryAccountBalanceRequest;
import com.aliyuncs.bssopenapi.model.v20171214.QueryAccountBalanceResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.ywxt.Dao.AliAccountDao;
import com.ywxt.Dao.Impl.AliAccountDaoImpl;
import com.ywxt.Dao.Impl.AliCdnDaoImpl;
import com.ywxt.Dao.Impl.AliEcsDaoImpl;
import com.ywxt.Domain.AliAccount;
import com.ywxt.Domain.AliEcs;
import com.ywxt.Service.Ali.AliAccountService;
import com.ywxt.Utils.Parameter;
import com.ywxt.Utils.TelegramUtils;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AliAccountServiceImpl implements AliAccountService {

    private AliAccountDao aliAccountDao = new AliAccountDaoImpl();

    // 列表
    public List<AliAccount> getList() {
        return this.aliAccountDao.getAliAccounts();
    }

    // 列表&校验金额
    public List<AliAccount> getList(boolean checkMoney) throws Exception {
        List<AliAccount> list = this.aliAccountDao.getAliAccounts();
        if (checkMoney) {
            for (AliAccount aa : list) {
                if (aa.getStatus().equals("normal")) {
                    QueryAccountBalanceResponse.Data data = new AliServiceImpl(aa.getAccessKeyId(), aa.getAccessKeySecret()).getAccountBalance();
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
        } else {
            aliAccount.setStatus("invalid");
        }
        // update ali Data
        new AliServiceImpl(aliAccount.getAccessKeyId(), aliAccount.getAccessKeySecret()).freshSourceData();
        return this.aliAccountDao.saveAliAccount(aliAccount);
    }

    // 删除账号
    public boolean deleteAccount(int aliAccountId) {
        // update ali Data
        AliAccount aliAccount = this.aliAccountDao.getAliAccount(aliAccountId);
        // 删除ecs
        new AliEcsDaoImpl().deleteAliEcsByAccessId(aliAccount.getAccessKeyId());
        // 删除cdn
        new AliCdnDaoImpl().deleteAliCdnByAccessId(aliAccount.getAccessKeyId());
        return this.aliAccountDao.deleteAliAccount(aliAccountId);
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