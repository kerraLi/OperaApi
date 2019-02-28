package com.ywxt.Command;

import com.ywxt.Dao.Ali.Impl.AliAccountDaoImpl;
import com.ywxt.Domain.Ali.AliAccount;
import com.ywxt.Domain.Ali.AliEcs;
import com.ywxt.Service.Ali.Impl.AliAccountServiceImpl;
import com.ywxt.Service.Ali.Impl.AliEcsServiceImpl;
import com.ywxt.Service.Ali.Impl.AliServiceImpl;
import com.ywxt.Utils.TelegramUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class CheckAli extends Check {

    // 校验余额
    @Scheduled(cron = "0 0 0/1 * * ?")
    private static void checkAccount() {
        try {
            List<AliAccount> list = new AliAccountServiceImpl().getList(true);
            String action = "ALI_ACCOUNT_NO_MONEY";
            for (AliAccount aliAccount : list) {
                if (aliAccount.getAlertBalance() && (!aliAccount.getAlertMarked())) {
                    Map<String, String> param = new HashMap<String, String>();
                    param.put("accountName", aliAccount.getUserName());
                    param.put("balance", aliAccount.getBalanceData().getAvailableAmount());
                    setMessage(action, aliAccount.getUserName(), param);
                    TelegramUtils.sendMessage(action, param);
                }
            }
        } catch (Exception e) {
            sendException("ALI", "account", e);
        }
    }

    // 校验ecs服务器过期
    @Scheduled(cron = "0 10 0/5 * * ?")
    private static void checkEcsExpired() {
        try {
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
        } catch (Exception e) {
            CheckAli.sendException("ALI", "ecs", e);
        }

    }

    // 刷新数据
    @Scheduled(cron = "0 0 0/5 * * ?")
    private static void refreshData() {
        try {
            List<AliAccount> list = new AliAccountServiceImpl().getList();
            for (AliAccount aliAccount : list) {
                if (aliAccount.getStatus().equals("normal")) {
                    new AliServiceImpl(aliAccount.getAccessKeyId(), aliAccount.getAccessKeySecret()).freshSourceData();
                }
            }
        } catch (Exception e) {
            CheckAli.sendException("ALI", "refresh", e);
        }
    }

}
