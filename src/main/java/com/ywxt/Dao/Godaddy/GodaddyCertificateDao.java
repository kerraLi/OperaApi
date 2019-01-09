package com.ywxt.Dao.Godaddy;

import com.ywxt.Domain.Godaddy.GodaddyCertificate;

import java.util.HashMap;
import java.util.List;

public interface GodaddyCertificateDao {
    public abstract int getCertificateTotal(HashMap<String, Object> params) throws Exception;

    public abstract List<GodaddyCertificate> getCertificateList(HashMap<String, Object> params) throws Exception;

    public abstract List<GodaddyCertificate> getCertificateList(HashMap<String, Object> params, int pageNumber, int pageSize) throws Exception;

    public abstract void saveCertificates(List<GodaddyCertificate> list);

    public abstract int saveCertificate(GodaddyCertificate godaddyCertificate);

    public abstract void deleteCertificateByAccessId(String accessId);
}
