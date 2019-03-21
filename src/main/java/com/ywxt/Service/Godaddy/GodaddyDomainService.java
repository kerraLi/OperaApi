package com.ywxt.Service.Godaddy;

import com.ywxt.Domain.Godaddy.GodaddyDomain;
import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface GodaddyDomainService {

    // domain-查询报警域名
    List<GodaddyDomain> getAlertList();

    // domain-查询所有域名&分页
    Page<GodaddyDomain> getList(Map<String, String> params) throws Exception;
}
