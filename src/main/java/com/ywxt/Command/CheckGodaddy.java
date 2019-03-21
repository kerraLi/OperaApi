package com.ywxt.Command;

import com.ywxt.Domain.Godaddy.GodaddyAccount;
import com.ywxt.Domain.Godaddy.GodaddyCertificate;
import com.ywxt.Domain.Godaddy.GodaddyDomain;
import com.ywxt.Service.Godaddy.GodaddyCertificateService;
import com.ywxt.Service.Godaddy.GodaddyDomainService;
import com.ywxt.Service.Godaddy.GodaddyService;
import com.ywxt.Service.Godaddy.Impl.GodaddyAccountServiceImpl;
import com.ywxt.Utils.TelegramUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CheckGodaddy extends Check {

    @Autowired
    private GodaddyService godaddyService;
    @Autowired
    private GodaddyCertificateService godaddyCertificateService;
    @Autowired
    private GodaddyDomainService godaddyDomainService;

    // 校验域名有效期
    @Scheduled(cron = "0 10 0/5 * * ?")
    private void checkDomain() {
        try {
            List<GodaddyDomain> list = godaddyDomainService.getAlertList();
            String action = "GODADDY_DOMAIN_EXPIRED_NUM";
            int count = 0;
            for (GodaddyDomain domain : list) {
                count++;
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

    // 校验证书有效期
    @Scheduled(cron = "0 10 0/5 * * ?")
    private void checkCertificate() {
        try {
            List<GodaddyCertificate> list = godaddyCertificateService.getAlertList();
            String action = "GODADDY_CERTIFICATE_EXPIRED_NUM";
            int count = 0;
            for (GodaddyCertificate certificate : list) {
                count++;
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
    private void refreshData() {
        try {
            List<GodaddyAccount> list = new GodaddyAccountServiceImpl().getList();
            for (GodaddyAccount godaddyAccount : list) {
                if (godaddyAccount.getStatus().equals("normal")) {
                    godaddyService.freshSourceData(godaddyAccount.getAccessKeyId(), godaddyAccount.getAccessKeySecret());
                }
            }
        } catch (Exception e) {
            sendException("GODADDY", "refresh", e);
        }
    }
}
