package com.ywxt.Command;

import com.ywxt.Domain.Ali.AliAccount;
import com.ywxt.Domain.Ali.AliEcs;
import com.ywxt.Service.Ali.AliAccountService;
import com.ywxt.Service.Ali.AliEcsService;
import com.ywxt.Service.Ali.AliService;
import com.ywxt.Service.Ali.Impl.AliAccountServiceImpl;
import com.ywxt.Service.System.MessageService;
import com.ywxt.Utils.TelegramUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CheckAli {

    @Autowired
    private AliService aliService;
    @Autowired
    private AliAccountService aliAccountService;
    @Autowired
    private AliEcsService aliEcsService;
    @Autowired
    private MessageService messageService;

    // 校验余额
    @Scheduled(cron = "0 0 0/1 * * ?")
    private void checkAccount() {
        try {
            List<AliAccount> list = aliAccountService.getList(true);
            String action = "ALI_ACCOUNT_NO_MONEY";
            for (AliAccount aliAccount : list) {
                if (aliAccount.getIsAlertBalance() && (!aliAccount.getIsAlertMarked())) {
                    Map<String, String> param = new HashMap<String, String>();
                    param.put("accountName", aliAccount.getUserName());
                    param.put("balance", aliAccount.getBalanceData().getAvailableAmount());
                    messageService.create(action, aliAccount.getUserName(), param);
                    // telegram
                    TelegramUtils.sendMessage(action, param);
                }
            }
        } catch (Exception e) {
            TelegramUtils.sendException("ALI-account", e);
        }
    }

    // 校验ecs服务器过期
    @Scheduled(cron = "0 10 0/5 * * ?")
    private void checkEcsExpired() {
        try {
            List<AliEcs> list = aliEcsService.getAlertList();
            String action = "ALI_ECS_EXPIRED_NUM";
            int count = 0;
            for (AliEcs aliEcs : list) {
                count++;
            }
            if (count > 0) {
                Map<String, String> param = new HashMap<String, String>();
                param.put("count", count + "");
                messageService.create(action, action, param);
                TelegramUtils.sendMessage(action, param);
            }
        } catch (Exception e) {
            TelegramUtils.sendException("ALI-ecs", e);
        }

    }

    // 刷新数据
    @Scheduled(cron = "0 0 0/5 * * ?")
    private void refreshData() {
        try {
            List<AliAccount> list = new AliAccountServiceImpl().getList();
            for (AliAccount aliAccount : list) {
                if (aliAccount.getStatus().equals("normal")) {
                    aliService.freshSourceData(aliAccount.getAccessKeyId(), aliAccount.getAccessKeySecret());
                }
            }
        } catch (Exception e) {
            TelegramUtils.sendException("ALI-refresh", e);
        }
    }

}
