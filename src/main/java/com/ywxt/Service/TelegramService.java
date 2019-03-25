package com.ywxt.Service;

import com.ywxt.Service.System.ParameterService;
import com.ywxt.Utils.HttpUtils;
import com.ywxt.Utils.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("telegramService")
public class TelegramService {

    @Autowired
    private ParameterService parameterService;
    @Value("${tel.url}")
    private String telegramBotUrl;
    @Value("${tel.chat_id}")
    private String telegramChatId = "-374037814";
    // telegram bot
    private Map<String, String> telegramBotActions = new HashMap<String, String>() {{
        put("SEND_MESSAGE", "/sendMessage");
    }};


    /**
     * 内容：
     * 发送错误到telegram
     */
    public void sendException(String msg, Exception e) {
        try {
            Map<String, String> param = new HashMap<String, String>();
            param.put("message", msg + "-" + e.getMessage());
            param.put("class", e.getClass().toString());
            this.sendMessage("ERROR", param);
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    /**
     * 内容：
     * 时间、主体（ali/godaddy/tencent等）、账号、业务标题、业务内容
     */
    public void sendMessage(String action, Map<String, String> parameters) throws Exception {
        // context
        String context = Parameter.MessageActions.get(action);
        List<com.ywxt.Domain.System.Parameter> ps = parameterService.getList();
        for (com.ywxt.Domain.System.Parameter p : ps) {
            context = context.replace("{" + p.getKey() + "}", p.getValue());
        }
        // param
        for (Map.Entry<String, String> e : parameters.entrySet()) {
            String value = (e.getValue() == null) ? "" : e.getValue();
            context = context.replace("{" + e.getKey() + "}", value);
        }
        String param = "chat_id=" + telegramChatId + "&text=" + URLEncoder.encode(context, StandardCharsets.UTF_8);
        if (!telegramBotUrl.isEmpty()) {
            HttpUtils.sendConnGet(telegramBotUrl + telegramBotActions.get("SEND_MESSAGE"), param, new HashMap<String, String>());
        }
    }
}
