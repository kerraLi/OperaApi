package com.ywxt.Service.Godaddy;

import com.ywxt.Domain.Godaddy.GodaddyCertificate;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface GodaddyCertificateService {

    // certificates-查询报警证书
    List<GodaddyCertificate> getAlertList();

    // certificates-查询所有证书&分页
    Page<GodaddyCertificate> getList(Map<String, String> params) throws Exception;
}
