package com.ywxt.Command;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ywxt.Service.Impl.GodaddyServiceImpl;
import com.ywxt.Utils.Parameter;
import com.ywxt.Utils.TelegramUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class CheckGodaddy {


    // 校验域名有效期
    private static void checkDomain() throws Exception {
        for (Map.Entry<String, String> e : Parameter.godaddyAccounts.entrySet()) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
            df.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, Integer.parseInt(Parameter.alertThresholds.get("GODADDY_DOMAIN_EXPIRED_DAY")));
            Date thresholdDate = calendar.getTime();
            GodaddyServiceImpl service = new GodaddyServiceImpl(e.getKey(), e.getValue());
            // 分页查找每页50
            int limit = 50;
            String markerDomain = "";
            while (true) {
                JSONArray jsonArray = service.getDomainList(limit, markerDomain);
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject domain = (JSONObject) jsonArray.get(i);
                    Date expiredTime = df.parse(domain.get("expires").toString().replace("Z", " UTC"));
                    if (expiredTime.before(thresholdDate) && domain.get("status") == "ACTIVE") {
                        DateFormat dfOut = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
                        Map<String, String> param = new HashMap<String, String>();
                        param.put("accountName", Parameter.godaddyAccountNames.get(e.getKey()));
                        param.put("domainId", domain.get("domainId").toString());
                        param.put("domain", domain.get("domain").toString());
                        param.put("expiredTime", dfOut.format(expiredTime));
                        param.put("expirationProtected", domain.get("expirationProtected").toString());
                        TelegramUtils.sendMessage("GODADDY_DOMAIN_EXPIRED", param);
                    }
                }
                if (jsonArray.size() < limit) {
                    break;
                }
                markerDomain = ((JSONObject) jsonArray.get(jsonArray.size() - 1)).get("domain").toString();
            }
        }
    }

    // 校验证书有效期
    private static void checkCertifate() throws Exception {
        for (Map.Entry<String, String> e : Parameter.godaddyAccounts.entrySet()) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            df.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, Integer.parseInt(Parameter.alertThresholds.get("GODADDY_CERTIFICATE_EXPIRED_DAY")));
            Date thresholdDate = calendar.getTime();
            JSONArray jsonArray = new GodaddyServiceImpl(e.getKey(), e.getValue()).getCertificateList();
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject certificate = (JSONObject) jsonArray.get(i);
                // 签发状态证书
                if (certificate.get("certificateStatus").toString().equals("ISSUED")) {
                    Date expiredTime = df.parse(certificate.get("validEnd").toString());
                    if (expiredTime.before(thresholdDate)) {
                        JSONArray subjectAlternativeNamesArray = JSONArray.parseArray(certificate.get("subjectAlternativeNames").toString());
                        String subjectAlternativeNames = "";
                        for (int ii = 0; ii < subjectAlternativeNamesArray.size(); ii++) {
                            JSONObject subject = (JSONObject) subjectAlternativeNamesArray.get(ii);
                            subjectAlternativeNames = subjectAlternativeNames.concat(subject.get("subjectAlternativeName") + ",");
                        }
                        subjectAlternativeNames = subjectAlternativeNames.substring(0, subjectAlternativeNames.length() - 1);
                        DateFormat dfOut = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
                        Map<String, String> param = new HashMap<String, String>();
                        param.put("accountName", Parameter.godaddyAccountNames.get(e.getKey()));
                        param.put("certificateId", certificate.get("certificateId").toString());
                        param.put("domain", certificate.get("commonName").toString());
                        param.put("expiredTime", dfOut.format(expiredTime));
                        param.put("subjectAlternativeNames", subjectAlternativeNames);
                        TelegramUtils.sendMessage("GODADDY_CERTIFICATE_EXPIRED", param);
                    }
                }
            }
        }
    }

    //    private static  void check
    public static void main(String[] args) throws Exception {
        try {
            CheckGodaddy.checkDomain();
            CheckGodaddy.checkCertifate();
        } catch (Exception e) {
            Map<String, String> param = new HashMap<String, String>();
            param.put("message", e.getMessage());
            param.put("class", e.getClass().toString());
            TelegramUtils.sendMessage("ERROR", param);
        }
    }

}
