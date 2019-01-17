package com.ywxt.Command;

import com.ywxt.Dao.Ali.Impl.AliAccountDaoImpl;
import com.ywxt.Domain.Ali.AliAccount;
import com.ywxt.Domain.Ali.AliEcs;
import com.ywxt.Service.Ali.Impl.AliAccountServiceImpl;
import com.ywxt.Service.Ali.Impl.AliEcsServiceImpl;
import com.ywxt.Service.Ali.Impl.AliServiceImpl;
import com.ywxt.Service.Impl.MessageServiceImpl;
import com.ywxt.Utils.TelegramUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class CheckAli {

    // 校验余额
    private static void checkAccount() throws Exception {
        List<AliAccount> list = new AliAccountServiceImpl().getList(true);
        String action = "ALI_ACCOUNT_NO_MONEY";
        for (AliAccount aliAccount : list) {
            if (aliAccount.getAlertBalance()) {
                Map<String, String> param = new HashMap<String, String>();
                param.put("accountName", aliAccount.getUserName());
                param.put("balance", aliAccount.getBalanceData().getAvailableAmount());
                setMessage(action, aliAccount.getUserName(), param);
                TelegramUtils.sendMessage(action, param);
            }
        }
    }

    // 校验ecs服务器过期
    private static void checkEcsExpired() throws Exception {
        List<AliEcs> list = new AliEcsServiceImpl().getEcsList(new HashMap<String, Object>() {{
        }});
        String action = "ALI_ECS_EXPIRED";
        for (AliEcs aliEcs : list) {
            if (aliEcs.getAlertExpired() && !aliEcs.getAlertMarked()) {
                DateFormat dfOut = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
                Map<String, String> param = new HashMap<String, String>();
                param.put("accountName", new AliAccountDaoImpl().getAliAccount(aliEcs.getAccessKeyId()).getUserName());
                param.put("ecsId", aliEcs.getInstanceId());
                param.put("ecsName", aliEcs.getInstanceName());
                param.put("expiredTime", dfOut.format(aliEcs.getExpiredTime()));
                setMessage(action, aliEcs.getInstanceId(), param);
                TelegramUtils.sendMessage(action, param);
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

    // 设置消息
    private static void setMessage(String action, String themeId, Map<String, String> parameters) throws Exception {
        new MessageServiceImpl().create(action, themeId, parameters, new HashMap<String, String>());
    }

    // export
    public static void exportCheck(String action) {
        try {
            switch (action) {
                case "refresh":
                    CheckAli.refreshData();
                    break;
                case "account":
                    CheckAli.checkAccount();
                    break;
                case "ecsExpired":
                    CheckAli.checkEcsExpired();
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            try {
                Map<String, String> param = new HashMap<String, String>();
                param.put("message", e.getMessage());
                param.put("class", e.getClass().toString());
                TelegramUtils.sendMessage("ERROR", param);
            } catch (Exception e2) {
                System.out.println(e2.getMessage());
            }
        }
    }

    // run
    public static void main(String[] args) throws Exception {
        try {
            CheckAli.refreshData();
            CheckAli.checkAccount();
            CheckAli.checkEcsExpired();
        } catch (Exception e) {
            Map<String, String> param = new HashMap<String, String>();
            param.put("message", e.getMessage());
            param.put("class", e.getClass().toString());
            TelegramUtils.sendMessage("ERROR", param);
        }
    }
}
