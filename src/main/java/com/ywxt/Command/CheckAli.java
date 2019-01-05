package com.ywxt.Command;

import com.aliyuncs.bssopenapi.model.v20171214.QueryAccountBalanceResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import com.ywxt.Service.Impl.AliServiceImpl;
import com.ywxt.Utils.Parameter;
import com.ywxt.Utils.TelegramUtils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class CheckAli {

    // 校验余额
    private static void checkAccount() throws Exception {
        System.out.println("test");
        for (Map.Entry<String, String> e : Parameter.aliAccounts.entrySet()) {
            QueryAccountBalanceResponse.Data data = new AliServiceImpl(e.getKey(), e.getValue()).getAccountBalance();
            // ali 金额 带千分符(,)
            if (new DecimalFormat().parse(data.getAvailableAmount()).doubleValue() <= Double.parseDouble(Parameter.alertThresholds.get("ALI_ACCOUNT_BALANCE"))) {
                Map<String, String> param = new HashMap<String, String>();
                param.put("accountName", Parameter.aliAccountNames.get(e.getKey()));
                param.put("balance", data.getAvailableAmount());
                TelegramUtils.sendMessage("ALI_ACCOUNT_NO_MONEY", param);
            }
        }
    }

    // 校验ecs服务器过期
    private static void checkEcsExpired() throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm Z");
        df.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, Integer.parseInt(Parameter.alertThresholds.get("ALI_ECS_EXPIRED_DAY")));
        Date thresholdDate = calendar.getTime();
        for (Map.Entry<String, String> e : Parameter.aliAccounts.entrySet()) {

            int pageNumber = 1;
            int pageSize = 50;

            while (true) {
            }
            for (DescribeInstancesResponse.Instance instance : new AliServiceImpl(e.getKey(), e.getValue()).getEcsList()) {
                Date expiredTime = df.parse(instance.getExpiredTime().replace("Z", " UTC"));
                if (expiredTime.before(thresholdDate)) {
                    DateFormat dfOut = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
                    Map<String, String> param = new HashMap<String, String>();
                    param.put("accountName", Parameter.aliAccountNames.get(e.getKey()));
                    param.put("ecsId", instance.getInstanceId());
                    param.put("ecsName", instance.getInstanceName());
                    param.put("expiredTime", dfOut.format(expiredTime));
                    TelegramUtils.sendMessage("ALI_ECS_EXPIRED", param);
                }
            }
        }
    }

    // run
    public static void main(String[] args) throws Exception {
        try {
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
