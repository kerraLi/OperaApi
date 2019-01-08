package com.ywxt.Command;

import com.aliyuncs.bssopenapi.model.v20171214.QueryAccountBalanceResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import com.ywxt.Dao.Impl.AliAccountDaoImpl;
import com.ywxt.Domain.AliAccount;
import com.ywxt.Domain.AliEcs;
import com.ywxt.Service.Ali.Impl.AliAccountServiceImpl;
import com.ywxt.Service.Ali.Impl.AliServiceImpl;
import com.ywxt.Utils.Parameter;
import com.ywxt.Utils.TelegramUtils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class CheckAli {

    // 校验余额
    private static void checkAccount() throws Exception {
        List<AliAccount> list = new AliAccountServiceImpl().getList(true);
        for (AliAccount aliAccount : list) {
            if (aliAccount.getAlertBalance()) {
                Map<String, String> param = new HashMap<String, String>();
                param.put("accountName", aliAccount.getUserName());
                param.put("balance", aliAccount.getBalanceData().getAvailableAmount());
                TelegramUtils.sendMessage("ALI_ACCOUNT_NO_MONEY", param);
            }
        }
    }

    // 校验ecs服务器过期
    private static void checkEcsExpired() throws Exception {
        List<AliEcs> list = new AliServiceImpl().getEcsList(new HashMap<String, Object>() {{
        }});
        for (AliEcs aliEcs : list) {
            if (aliEcs.getAlertExpired()) {
                DateFormat dfOut = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
                Map<String, String> param = new HashMap<String, String>();
                param.put("accountName", new AliAccountDaoImpl().getAliAccount(aliEcs.getAccessKeyId()).getUserName());
                param.put("ecsId", aliEcs.getInstanceId());
                param.put("ecsName", aliEcs.getInstanceName());
                param.put("expiredTime", dfOut.format(aliEcs.getExpiredTime()));
                TelegramUtils.sendMessage("ALI_ECS_EXPIRED", param);
            }
        }
    }

    // 刷新数据
    private static void refreshData() throws Exception {
        List<AliAccount> list = new AliAccountServiceImpl().getList();
        for (AliAccount aliAccount : list) {
            if (aliAccount.getStatus().equals("normal")) {
                new AliServiceImpl(aliAccount.getAccessKeyId(), aliAccount.getAccessKeySecret()).freshSourceData();
            }
        }
    }

    // run
    public static void main(String[] args) throws Exception {
        try {
            System.out.println("0");
            CheckAli.refreshData();
            System.out.println("1");
            CheckAli.checkAccount();
            System.out.println("2");
            CheckAli.checkEcsExpired();
        } catch (Exception e) {
            Map<String, String> param = new HashMap<String, String>();
            param.put("message", e.getMessage());
            param.put("class", e.getClass().toString());
            TelegramUtils.sendMessage("ERROR", param);
        }
    }
}
