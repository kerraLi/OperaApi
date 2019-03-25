package com.ywxt.Command;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Service.System.MessageService;
import com.ywxt.Service.System.ParameterService;
import com.ywxt.Service.TelegramService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RabbitListener(queues = "monitor")
public class MqConsumer {

    @Autowired
    private MessageService messageService;
    @Autowired
    private TelegramService telegramService;
    @Autowired
    private ParameterService parameterService;

    @RabbitListener(queues = "monitor")
    public void onMessage(Message message) {
        try {
            JSONObject obj = JSONObject.parseObject(new String(message.getBody()));
            String action = obj.get("action").toString();
            // 时间监控
            if (action.equals("monitor_time")) {
                int subTime = Integer.parseInt(parameterService.getValue("MONITORTIME_TIMEOUT_SECOND"));
                JSONObject result = JSONObject.parseObject(obj.get("result").toString());
                Long serverT = Long.parseLong(result.get("timestamp").toString());
                Long currentT = System.currentTimeMillis() / 1000;
                if (currentT - serverT > subTime || serverT - currentT > subTime) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Map<String, String> param = new HashMap<String, String>();
                    param.put("server_ip", obj.get("server_ip").toString());
                    param.put("server_name", obj.get("server_name").toString());
                    param.put("server_time", sdf.format(new Date(serverT * 1000)));
                    param.put("current_time", sdf.format(new Date(currentT * 1000)));
                    // message
                    messageService.create("MONITORTIME_TIMEOUT", param.get("server_ip"), param);
                    // telegram
                    telegramService.sendMessage("MONITORTIME_TIMEOUT", param);
                }
            }
        } catch (Exception e) {
            telegramService.sendException("MONITOR", e);
            e.printStackTrace();
        }
    }
}
