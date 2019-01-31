package com.ywxt.Command;

import com.ywxt.Domain.Aws.AwsAccount;
import com.ywxt.Service.Aws.Impl.AwsAccountServiceImpl;
import com.ywxt.Service.Aws.Impl.AwsServiceImpl;
import com.ywxt.Service.Impl.MessageServiceImpl;
import com.ywxt.Utils.TelegramUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckAws {
    // 刷新数据
    private static void refreshData() throws Exception {
        List<AwsAccount> list = new AwsAccountServiceImpl().getList();
        for (AwsAccount awsAccount : list) {
            if (awsAccount.getStatus().equals("normal")) {
                new AwsServiceImpl(awsAccount.getAccessKeyId(), awsAccount.getAccessKeySecret()).freshSourceData();
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
                    CheckAws.refreshData();
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            try {
                Map<String, String> param = new HashMap<String, String>();
                param.put("message", "Aws-" + action + "-" + e.getMessage());
                param.put("class", e.getClass().toString());
                TelegramUtils.sendMessage("ERROR", param);
                CheckAws.printException(e);
            } catch (Exception e2) {
                CheckAws.printException(e2);
            }
        }
    }

    // run
    public static void main(String[] args) throws Exception {
        try {
            CheckAws.refreshData();
        } catch (Exception e) {
            Map<String, String> param = new HashMap<String, String>();
            param.put("message", e.getMessage());
            param.put("class", e.getClass().toString());
            TelegramUtils.sendMessage("ERROR", param);
            CheckAws.printException(e);
        }
    }

    // 输出错误（记录日志中排查问题）
    private static void printException(Exception e) {
        StackTraceElement[] ses = e.getStackTrace();
        System.err.println("Exception " + e.toString());
        for (StackTraceElement se : ses) {
            System.err.println("\tat " + se);
        }
    }

}
