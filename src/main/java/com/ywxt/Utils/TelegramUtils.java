package com.ywxt.Utils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TelegramUtils {

    /**
     * 内容：
     * 时间、主体（ali/godaddy/tencent等）、账号、业务标题、业务内容
     */
    public static void sendMessage(String action, Map<String, String> parameters) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        Map<String, String> actions = new HashMap<String, String>() {{
            put("ERROR", df.format(new Date()) + "\r\n错误类：{class}\r\n错误信息{message}");
            put("ALI_ACCOUNT_NO_MONEY", df.format(new Date()) + "\r\n您好，阿里云账号余额已少于" + Parameter.alertThresholds.get("ALI_ACCOUNT_BALANCE") + "元，请及时充值。\r\n友情链接：https://www.aliyun.com/\r\n账号：{accountName}\r\n当前余额{balance}");
            put("ALI_ECS_EXPIRED", df.format(new Date()) + "\r\n您好，阿里云服务器有效时间已少于" + Parameter.alertThresholds.get("ALI_ECS_EXPIRED_DAY") + "天，请及时续费。\r\n友情链接：https://www.aliyun.com/\r\n账号：{accountName}\r\n服务器ID:{ecsId}\r\n服务器名称:{ecsName}\r\n过期时间：{expiredTime}");
            put("GODADDY_DOMAIN_EXPIRED", df.format(new Date()) + "\r\n您好，Godaddy域名有效时间已少于" + Parameter.alertThresholds.get("GODADDY_DOMAIN_EXPIRED_DAY") + "天，请及时续费。\r\n友情链接：https://www.godaddy.com/\r\n账号：{accountName}\r\n域名ID:{domainId}\r\n域名:{domain}\r\n过期时间：{expiredTime}\r\n到期是否受保护：{expirationProtected}");
            put("GODADDY_CERTIFICATE_EXPIRED", df.format(new Date()) + "\r\n您好，Godaddy证书有效时间已少于" + Parameter.alertThresholds.get("GODADDY_CERTIFICATE_EXPIRED_DAY") + "天，请及时续费。\r\n友情链接：https://www.godaddy.com/\r\n账号：{accountName}\r\n证书ID:{certificateId}\r\n域名:{domain}\r\n过期时间：{expiredTime}\r\n主体备选域名：{subjectAlternativeNames}");
        }};
        String context = actions.get(action);
        for (Map.Entry<String, String> e : parameters.entrySet()) {
            context = context.replace("{" + e.getKey() + "}", e.getValue());
        }
        String param = "chat_id=" + Parameter.telegramChatId + "&text=" + URLEncoder.encode(context, StandardCharsets.UTF_8);
        HttpUtils.sendConnGet(Parameter.telegramBotUrl + Parameter.telegramBotActions.get("SEND_MESSAGE"), param, new HashMap<String, String>());
    }

    //主函数调用测试
    // public static void main(String[] args) {
    //     System.out.println("telegram");
    //     TelegramUtils.sendMessage("ALI_ACCOUNT_NO_MONEY", new HashMap<String, String>() {{
    //         put("accountName", "test");
    //         put("balance", "5000.00");
    //     }});
    // }
}
