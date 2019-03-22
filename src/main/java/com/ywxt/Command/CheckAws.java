package com.ywxt.Command;

import com.ywxt.Domain.Aws.AwsAccount;
import com.ywxt.Service.Aws.AwsAccountService;
import com.ywxt.Service.Aws.AwsService;
import com.ywxt.Service.TelegramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CheckAws {

    @Autowired
    private AwsService awsService;
    @Autowired
    private AwsAccountService awsAccountService;
    @Autowired
    private TelegramService telegramService;

    // 刷新数据
    @Scheduled(cron = "0 0 0/5 * * ?")
    private void refreshData() {
        try {
            List<AwsAccount> list = awsAccountService.getList();
            for (AwsAccount awsAccount : list) {
                if (awsAccount.getStatus().equals("normal")) {
                    awsService.freshSourceData(awsAccount.getAccessKeyId(), awsAccount.getAccessKeySecret());
                }
            }
        } catch (Exception e) {
            telegramService.sendException("AWS-refresh", e);
        }
    }

}
