package com.ywxt.Service.Ali.Impl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.bssopenapi.model.v20171214.QueryAccountBalanceRequest;
import com.aliyuncs.bssopenapi.model.v20171214.QueryAccountBalanceResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.ywxt.Dao.Ali.AliAccountDao;
import com.ywxt.Domain.Ali.AliAccount;
import com.ywxt.Handler.AsyncHandler;
import com.ywxt.Service.Ali.AliAccountService;
import com.ywxt.Service.Ali.AliService;
import com.ywxt.Service.System.IgnoreService;
import com.ywxt.Service.System.ParameterService;
import com.ywxt.Utils.ArrayUtils;
import com.ywxt.Utils.AsyncUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;

@Service("aliAccountService")
public class AliAccountServiceImpl implements AliAccountService {

    @Autowired
    private AliAccountDao aliAccountDao;
    @Autowired
    private AliService aliService;
    @Autowired
    private IgnoreService ignoreService;
    @Autowired
    private ParameterService parameterService;

    // 获取账户
    public AliAccount getAliAccount(int id) {
        return aliAccountDao.getOne(id);
    }

    // 列表
    public List<AliAccount> getList() {
        return aliAccountDao.findAll();
    }

    // 列表&校验金额
    public List<AliAccount> getList(boolean checkMoney) throws Exception {
        List<AliAccount> list = aliAccountDao.findAll();
        if (checkMoney) {
            // 是否弃用标记
            String[] markValues = ignoreService.getMarkedValues("AliAccount");
            for (AliAccount aa : list) {
                if (aa.getStatus().equals("normal")) {
                    QueryAccountBalanceResponse.Data data = this.getAccountBalance(aa.getAccessKeyId(), aa.getAccessKeySecret());
                    aa.setBalanceData(data);
                    // ali 金额 带千分符(,)
                    if (new DecimalFormat().parse(data.getAvailableAmount()).doubleValue()
                            <= Double.parseDouble(parameterService.getValue("ALI_ACCOUNT_BALANCE"))) {
                        aa.setAlertBalance(true);
                    }
                }
                if (ArrayUtils.hasString(markValues, aa.getAccessKeyId())) {
                    aa.setAlertMarked(true);
                }
            }
        }
        return list;
    }

    // 新增/修改
    public AliAccount saveAliAccount(AliAccount aliAccount) throws Exception {
        // check ali key
        if (this.checkAccount(aliAccount.getAccessKeyId(), aliAccount.getAccessKeySecret())) {
            aliAccount.setStatus("normal");
            // update Data & 异步
            AsyncHandler handler = new AsyncHandler() {
                @Override
                public void handle() {
                    try {
                        aliService.freshSourceData(aliAccount.getAccessKeyId(), aliAccount.getAccessKeySecret());
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
        return aliAccountDao.save(aliAccount);
    }

    // 删除账号
    public void deleteAccount(int id) {
        // update ali Data
        AliAccount aliAccount = aliAccountDao.getOne(id);
        if (aliAccount.getStatus().equals("normal")) {
            // update Data & 异步
            AsyncHandler handler = new AsyncHandler() {
                @Override
                public void handle() {
                    aliService.removeSourceData(aliAccount.getAccessKeyId());
                }
            };
            AsyncUtils.asyncWork(handler);
        }
        aliAccountDao.deleteById(id);
    }

    // 校验密钥
    private boolean checkAccount(String accessKeyId, String accessKeySecret) throws Exception {
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

    // 获取ali账户余额
    private QueryAccountBalanceResponse.Data getAccountBalance(String keyId, String keySecret) throws Exception {
        IAcsClient client = aliService.getAliClient("", keyId, keySecret);
        QueryAccountBalanceRequest request = new QueryAccountBalanceRequest();
        request.setEndpoint("business.aliyuncs.com");
        QueryAccountBalanceResponse response = client.getAcsResponse(request);
        return response.getData();
    }

}