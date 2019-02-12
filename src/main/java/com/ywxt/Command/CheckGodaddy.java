package com.ywxt.Command;

import com.ywxt.Dao.Godaddy.Impl.GodaddyAccountDaoImpl;
import com.ywxt.Domain.Godaddy.GodaddyAccount;
import com.ywxt.Domain.Godaddy.GodaddyCertificate;
import com.ywxt.Domain.Godaddy.GodaddyDomain;
import com.ywxt.Service.Godaddy.Impl.GodaddyAccountServiceImpl;
import com.ywxt.Service.Godaddy.Impl.GodaddyCertificateServiceImpl;
import com.ywxt.Service.Godaddy.Impl.GodaddyDomainServiceImpl;
import com.ywxt.Service.Godaddy.Impl.GodaddyServiceImpl;
import com.ywxt.Utils.TelegramUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class CheckGodaddy extends Check {


    // 校验域名有效期
    @Scheduled(cron = "* * 0/5 * * ?")
    private static void checkDomain() throws Exception {
        try {
            List<GodaddyDomain> list = new GodaddyDomainServiceImpl().getDomainList(new HashMap<String, Object>() {{
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
                    TelegramUtils.sendMessage(action, param);
                }
            }
        } catch (Exception e) {
            sendException("GODADDY", "domain", e);
        }
    }

    // 校验证书有效期
    @Scheduled(cron = "* * 0/5 * * ?")
    private static void checkCertificate() throws Exception {
        try {
            List<GodaddyCertificate> list = new GodaddyCertificateServiceImpl().getCertificateList(new HashMap<String, Object>() {{
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
                    TelegramUtils.sendMessage(action, param);
                }
            }
        } catch (Exception e) {
            sendException("GODADDY", "certificate", e);
        }
    }

    // 刷新数据
    @Scheduled(cron = "* * 0/5 * * ?")
    private static void refreshData() throws Exception {
        try {
            List<GodaddyAccount> list = new GodaddyAccountServiceImpl().getList();
            for (GodaddyAccount godaddyAccount : list) {
                if (godaddyAccount.getStatus().equals("normal")) {
                    new GodaddyServiceImpl(godaddyAccount.getAccessKeyId(), godaddyAccount.getAccessKeySecret()).freshSourceData();
                }
            }
        } catch (Exception e) {
            sendException("GODADDY", "refresh", e);
        }
    }
}
