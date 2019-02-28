package com.ywxt.Service.Godaddy;

import com.ywxt.Domain.Godaddy.GodaddyCertificate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface GodaddyCertificateService {

    public abstract List<GodaddyCertificate> getCertificateList(HashMap<String, Object> params) throws Exception;

    public abstract Map<String, Object> getCertificateList(HashMap<String, Object> params, int pageNumber, int pageSize) throws Exception;
}
