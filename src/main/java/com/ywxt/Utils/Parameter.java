package com.ywxt.Utils;

import java.util.HashMap;
import java.util.Map;

public class Parameter {
    private Parameter() {
    }

    // 登陆
    public static String defaultUsername = "admin";
    public static String defaultPassword = "e10adc3949ba59abbe56e057f20f883e";

    // telegram bot
    public static String telegramBotUrl = "https://api.telegram.org/bot739033903:AAHMeAiArCp6jmQIE3u3MT_wkV_vFX2oNK4";
    public static Map<String, String> telegramBotActions = new HashMap<String, String>() {{
        put("SEND_MESSAGE", "/sendMessage");
    }};
    public static String telegramChatId = "-374037814";

    /**
     * ali account
     */
    public static Map<String, String> aliAccounts = new HashMap<String, String>() {{
        put("LTAIDmBAC9rB3SlR", "C2FD6tBp8r8jE7PvtTJsi63IDZ4tFE");
        put("LTAIpCUPpQlpKV4M", "qqrvWAoW9bhVauW47JGMY05H2fXcgU");
        put("LTAIuVUxS5F55kMV", "dGUCUg0nPsm9tSPaWbLmx7ndBCO8Nu");
    }};
    public static Map<String, String> aliAccountNames = new HashMap<String, String>() {{
        put("LTAIDmBAC9rB3SlR", "gongtian");//develop@1077205130889846.onaliyun.com
        put("LTAIpCUPpQlpKV4M", "sanjing");//develop2@1083905136183150.onaliyun.com
        put("LTAIuVUxS5F55kMV", "kakasi");//kakasi@1865517655536431.onaliyun.com
    }};

    /**
     * godaddy account
     */
    public static String godaddyUrl = "https://api.godaddy.com";
    public static Map<String, String> godaddyActions = new HashMap<String, String>() {{
        put("GET_DOMAIN_LIST", "/v1/domains");
        put("GET_CERTIFICATE_LIST", "/v1/certificates");
    }};
    public static Map<String, String> godaddyAccounts = new HashMap<String, String>() {{
        // production
        put("dLDHSMnAAEN6_E1XDuVUJEhuwSpvrcCA59t", "E1axtvaUTwMNrQnzFoSFY6");
    }};
    public static Map<String, String> godaddyAccountNames = new HashMap<String, String>() {{
        // production
        put("dLDHSMnAAEN6_E1XDuVUJEhuwSpvrcCA59t", "yingmu007 production");
    }};

    // 报警阈值参数
    public static Map<String, String> alertThresholds = new HashMap<String, String>() {{
        // ali余额阈值
        put("ALI_ACCOUNT_BALANCE", "2000.00");
        // ali ecs 服务器剩余时间阈值（单位 天）
        put("ALI_ECS_EXPIRED_DAY", "5");
        // godaddy domain 域名剩余时间阈值（单位 天）
        put("GODADDY_DOMAIN_EXPIRED_DAY", "30");
        // godaddy certificate 证书剩余时间阈值（单位 天）
        put("GODADDY_CERTIFICATE_EXPIRED_DAY", "30");
    }};
}
