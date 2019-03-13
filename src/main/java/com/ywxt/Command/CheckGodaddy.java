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
    @Scheduled(cron = "0 10 0/5 * * ?")
    private static void checkDomain() throws Exception {
        try {
            List<GodaddyDomain> list = new GodaddyDomainServiceImpl().getDomainList(new HashMap<String, Object>() {{
            }});
            String action = "GODADDY_DOMAIN_EXPIRED_NUM";
            int count = 0;
            for (GodaddyDomain domain : list) {
                if (domain.isAlertExpired() && !domain.isAlertMarked()) {
                    count++;
                }
            }
            if (count > 0) {
                Map<String, String> param = new HashMap<String, String>();
                param.put("count", count + "");
                setMessage(action, action, param);
                TelegramUtils.sendMessage(action, param);
            }
        } catch (Exception e) {
            sendException("GODADDY", "domain", e);
        }
    }

    // todo
    // 校验证书有效期
    @Scheduled(cron = "0 10 0/5 * * ?")
    private static void checkCertificate() throws Exception {
        try {
            List<GodaddyCertificate> list = new GodaddyCertificateServiceImpl().getCertificateList(new HashMap<String, Object>() {{
            }});
            String action = "GODADDY_CERTIFICATE_EXPIRED_NUM";
            int count = 0;
            for (GodaddyCertificate certificate : list) {
                if (certificate.isAlertExpired() && !certificate.isAlertMarked()) {
                    count++;
                }
            }
            if (count > 0) {
                Map<String, String> param = new HashMap<String, String>();
                param.put("count", count + "");
                setMessage(action, action, param);
                TelegramUtils.sendMessage(action, param);
            }
        } catch (Exception e) {
            sendException("GODADDY", "certificate", e);
        }
    }

    // 刷新数据
    @Scheduled(cron = "0 0 0/5 * * ?")
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
