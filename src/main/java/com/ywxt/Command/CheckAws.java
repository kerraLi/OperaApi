package com.ywxt.Command;

import com.ywxt.Domain.Aws.AwsAccount;
import com.ywxt.Service.Aws.Impl.AwsAccountServiceImpl;
import com.ywxt.Service.Aws.Impl.AwsServiceImpl;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CheckAws extends Check {

    // 刷新数据
    @Scheduled(cron = "* * 0/5 * * ?")
    private static void refreshData() throws Exception {
        try {
            List<AwsAccount> list = new AwsAccountServiceImpl().getList();
            for (AwsAccount awsAccount : list) {
                if (awsAccount.getStatus().equals("normal")) {
                    new AwsServiceImpl(awsAccount.getAccessKeyId(), awsAccount.getAccessKeySecret()).freshSourceData();
                }
            }
        } catch (Exception e) {
            CheckAli.sendException("AWS", "refresh", e);
        }
    }

}
