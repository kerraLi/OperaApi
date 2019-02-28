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
        String context = Parameter.MessageActions.get(action);
        for (Map.Entry<String, String> e : parameters.entrySet()) {
            String value = (e.getValue() == null) ? "" : e.getValue();
            context = context.replace("{" + e.getKey() + "}", value);
        }
        String param = "chat_id=" + Parameter.telegramChatId + "&text=" + URLEncoder.encode(context, StandardCharsets.UTF_8);
        if (!Parameter.telegramBotUrl.isEmpty()) {
            HttpUtils.sendConnGet(Parameter.telegramBotUrl + Parameter.telegramBotActions.get("SEND_MESSAGE"), param, new HashMap<String, String>());
        }
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
