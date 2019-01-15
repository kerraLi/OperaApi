package com.ywxt.Command;

import com.ywxt.Dao.Godaddy.Impl.GodaddyAccountDaoImpl;
import com.ywxt.Domain.Godaddy.GodaddyAccount;
import com.ywxt.Domain.Godaddy.GodaddyCertificate;
import com.ywxt.Domain.Godaddy.GodaddyDomain;
import com.ywxt.Service.Godaddy.Impl.GodaddyAccountServiceImpl;
import com.ywxt.Service.Godaddy.Impl.GodaddyServiceImpl;
import com.ywxt.Service.Impl.MessageServiceImpl;
import com.ywxt.Utils.TelegramUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class CheckGodaddy {


    // 校验域名有效期
    private static void checkDomain() throws Exception {
        List<GodaddyDomain> list = new GodaddyServiceImpl().getDomainList(new HashMap<String, Object>() {{
        }});
        String action = "GODADDY_DOMAIN_EXPIRED";
        for (GodaddyDomain domain : list) {
            if (domain.isAlertExpired() && !domain.isAlertMarked()) {
                DateFormat dfOut = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
                Map<String, String> param = new HashMap<String, String>();
                param.put("accountName", new GodaddyAccountDaoImpl().getAccount(domain.getAccessKeyId()).getUserName());
                param.put("domainId", domain.getDomainId());
                param.put("domain", domain.getDomain());
                param.put("expiredTime", dfOut.format(domain.getExpires()));
                param.put("expirationProtected", String.valueOf(domain.isExpirationProtected()));
                setMessage(action, domain.getDomain(), param);
                TelegramUtils.sendMessage("", param);
            }
        }
    }

    // 校验证书有效期
    private static void checkCertifate() throws Exception {

        List<GodaddyCertificate> list = new GodaddyServiceImpl().getCertificateList(new HashMap<String, Object>() {{
        }});
        String action = "GODADDY_CERTIFICATE_EXPIRED";
        for (GodaddyCertificate certificate : list) {
            if (certificate.isAlertExpired() && !certificate.isAlertMarked()) {
                DateFormat dfOut = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
                Map<String, String> param = new HashMap<String, String>();
                param.put("accountName", new GodaddyAccountDaoImpl().getAccount(certificate.getAccessKeyId()).getUserName());
                param.put("certificateId", certificate.getCertificateId());
                param.put("domain", certificate.getCommonName());
                param.put("expiredTime", dfOut.format(certificate.getValidEnd()));
                param.put("subjectAlternativeNames", certificate.getSubjectAlternativeNames());
                setMessage(action, certificate.getCommonName(), param);
                TelegramUtils.sendMessage("GODADDY_CERTIFICATE_EXPIRED", param);
            }
        }
    }

    // 刷新数据
    private static void refreshDate() throws Exception {
        List<GodaddyAccount> list = new GodaddyAccountServiceImpl().getList();
        for (GodaddyAccount godaddyAccount : list) {
            if (godaddyAccount.getStatus().equals("normal")) {
                new GodaddyServiceImpl(godaddyAccount.getAccessKeyId(), godaddyAccount.getAccessKeySecret()).freshSourceData();
            }
        }
    }

    // 设置消息
    private static void setMessage(String action, String themeId, Map<String, String> parameters) throws Exception {
        new MessageServiceImpl().create(action, themeId, parameters, new HashMap<String, String>());
    }

    //    private static  void check
    public static void main(String[] args) throws Exception {
        try {
            CheckGodaddy.refreshDate();
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
