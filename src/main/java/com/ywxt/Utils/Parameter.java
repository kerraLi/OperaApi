package com.ywxt.Utils;

import com.ywxt.Service.System.Impl.ParameterServiceImpl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Parameter {
    private Parameter() {
    }

    // 环境变量["dev":"测试";"prod":"正式"]
    public static String ENV = "dev";
    // 调用本机websocket http
    public static String urlWebsocket = "http://10.10.24.56:9000/api/message/websocket";
    // test环境
    // public static String urlWebsocket = "http://47.75.103.32:8080/api/message/websocket";
    // redis
    public static String redisHost = "127.0.0.1";
    public static int redisPort = 63790;
    // test环境
    // public static int redisPort = 6379;
    public static String redisName = "master";
    // redis-key
    public static String redisKeyUserToken = "USER_TOKEN_{token}";
    public static String redisKeyMonitorSpeed = "MONITOR_SPEED_{code}";
    // redis-time(s)
    public static int redisTllUserToken = 60 * 60 * 2;
    public static int redisTllMonitorSpeed = 60 * 60;

    // 新建用户默认头像
    public static String defaultAvatar = "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif";
    // 登陆有效时间（ms）
    public static int loginTtlMs = 60 * 60 * 1000 * 2;
    // 登陆token密钥
    public static String authoTokenKey = "ywxt2019v1";
    // md5加密key（不要随意改动）
    public static String md5Key = "ywxt2019";

    // telegram bot
    // test环境
//    public static String telegramBotUrl = "";
    public static String telegramBotUrl = "https://api.telegram.org/bot739033903:AAHMeAiArCp6jmQIE3u3MT_wkV_vFX2oNK4";
    public static Map<String, String> telegramBotActions = new HashMap<String, String>() {{
        put("SEND_MESSAGE", "/sendMessage");
    }};
    public static String telegramChatId = "-374037814";

    /**
     * godaddy account
     */
    public static String godaddyUrl = "https://api.godaddy.com";
    public static Map<String, String> godaddyActions = new HashMap<String, String>() {{
        put("GET_DOMAIN_LIST", "/v1/domains");
        put("GET_CERTIFICATE_LIST", "/v1/certificates");
    }};

    private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
    public static Map<String, String> MessageActions = new HashMap<String, String>() {{
        put("ERROR", "错误类：{class}\r\n错误信息{message}");
        put("ALI_ACCOUNT_NO_MONEY", "您好，阿里云账号余额已少于{ALI_ACCOUNT_BALANCE}元，请及时充值。\r\n账号：{accountName}\r\n当前余额{balance}");
        // count
        put("ALI_ECS_EXPIRED_NUM", "您好，已有{count}台阿里云服务器有效时间已少于{ALI_ECS_EXPIRED_DAY}天，请及时登陆平台查看，续费。\r\n");
        put("GODADDY_DOMAIN_EXPIRED_NUM", "您好，已有{count}个GODADDY域名有效时间已少于{GODADDY_DOMAIN_EXPIRED_DAY}天，请及时登陆平台查看，续费。\r\n");
        put("GODADDY_CERTIFICATE_EXPIRED_NUM", "您好，已有{count}个GODADDY证书有效时间已少于{GODADDY_CERTIFICATE_EXPIRED_DAY}天，请及时登陆平台查看，续费。\r\n");
        // message
        put("ALI_ECS_EXPIRED", "您好，阿里云服务器有效时间已少于{ALI_ECS_EXPIRED_DAY}天，请及时续费。\r\n账号：{accountName}\r\n服务器ID:{ecsId}\r\n服务器名称:{ecsName}\r\n过期时间：{expiredTime}");
        put("GODADDY_DOMAIN_EXPIRED", "您好，Godaddy域名有效时间已少于{GODADDY_DOMAIN_EXPIRED_DAY}天，请及时续费。\r\n账号：{accountName}\r\n域名ID:{domainId}\r\n域名:{domain}\r\n过期时间：{expiredTime}\r\n到期是否受保护：{expirationProtected}");
        put("GODADDY_CERTIFICATE_EXPIRED", "您好，Godaddy证书有效时间已少于{GODADDY_CERTIFICATE_EXPIRED_DAY}天，请及时续费。\r\n账号：{accountName}\r\n证书ID:{certificateId}\r\n域名:{domain}\r\n过期时间：{expiredTime}\r\n主体备选域名：{subjectAlternativeNames}");
        put("WEBHOOK_MESSAGE", "*****报警信息*****\r\n标题：{ruleName}\r\n报警连接：{ruleUrl}\r\n状态:{state}\r\n内容：{title}\r\n具体消息：{message}");
    }};

    public static Map<String, String> MessageTitles = new HashMap<String, String>() {{
        put("ALI_ACCOUNT_NO_MONEY", "续费");
        put("ALI_ECS_EXPIRED_NUM", "ALI服务器过期");
        put("ALI_ECS_EXPIRED", "ALI服务器过期");
        put("GODADDY_DOMAIN_EXPIRED_NUM", "GO域名过期");
        put("GODADDY_DOMAIN_EXPIRED", "GO域名过期");
        put("GODADDY_CERTIFICATE_EXPIRED_NUM", "GO证书过期");
        put("GODADDY_CERTIFICATE_EXPIRED", "GO证书过期");
        put("WEBHOOK_MESSAGE", "WEBHOOK报警");
    }};

    /**
     * todo 特殊权限
     */
    public static Map<String, String> SpecialPermissions = new HashMap<String, String>() {{
        put("ALI_ACCOUNT_NO_MONEY", "续费");
        put("ALI_ECS_EXPIRED", "过期");
        put("ALI_ECS_EXPIRED_NUM", "过期");
        put("GODADDY_DOMAIN_EXPIRED", "过期");
        put("GODADDY_DOMAIN_EXPIRED_NUM", "过期");
        put("GODADDY_CERTIFICATE_EXPIRED", "过期");
        put("GODADDY_CERTIFICATE_EXPIRED_NUM", "过期");
        put("WEBHOOK_MESSAGE", "报警");
    }};

}
