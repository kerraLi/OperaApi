package com.ywxt.Service.Godaddy.Impl;

import com.ywxt.Dao.Godaddy.Impl.GodaddyAccountDaoImpl;
import com.ywxt.Dao.Godaddy.Impl.GodaddyCertificateDaoImpl;
import com.ywxt.Domain.Godaddy.GodaddyAccount;
import com.ywxt.Domain.Godaddy.GodaddyCertificate;
import com.ywxt.Service.Godaddy.GodaddyCertificateService;
import com.ywxt.Service.Impl.ParameterIgnoreServiceImpl;
import com.ywxt.Utils.ArrayUtils;
import com.ywxt.Utils.Parameter;

import java.util.*;

public class GodaddyCertificateServiceImpl extends GodaddyServiceImpl implements GodaddyCertificateService {

    private String accessKeyId;
    private String accessKeySecret;
    private HashMap<String, String> userNameMap = new HashMap<>();


    public GodaddyCertificateServiceImpl() {

    }

    public GodaddyCertificateServiceImpl(String accessKeyId) throws Exception {
        GodaddyAccount godaddyAccount = new GodaddyAccountDaoImpl().getAccount(accessKeyId);
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = godaddyAccount.getAccessKeySecret();
    }

    public GodaddyCertificateServiceImpl(String keyId, String keySecret) {
        this.accessKeyId = keyId;
        this.accessKeySecret = keySecret;
    }

    // 获取dash数据
    public HashMap<String, Object> getDashData() throws Exception {
        // normal invalid
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("accessKeyId", this.accessKeyId);
        List<Object[]> list = new GodaddyCertificateDaoImpl().getCountGroup(params);
        Long normal = 0L;
        Long invalid = 0L;
        for (Object[] os : list) {
            if (os[0].equals("ISSUED")) {
                normal = (Long) os[1];
            } else {
                invalid += (Long) os[1];
            }
        }
        // expired
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, Integer.parseInt(Parameter.alertThresholds.get("GODADDY_CERTIFICATE_EXPIRED_DAY")));
        Date thresholdDate = calendar.getTime();
        params.put("orderAsc", "validEnd");
        params.put("certificateStatus", "ISSUED");
        params.put("validEnd@lt", thresholdDate);
        params.put("accessKeyId", this.accessKeyId);
        int expired = this.getCertificateTotal(params);
        // deprecated
        params = new HashMap<String, Object>();
        params.put("ifMarked", "true");
        params.put("accessKeyId", this.accessKeyId);
        int deprecated = this.getCertificateTotal(params);
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("normal", normal);
        result.put("invalid", invalid);
        result.put("expired", expired);
        result.put("deprecated", deprecated);
        return result;
    }

    // ecs-获取个数
    public int getCertificateTotal(HashMap<String, Object> params) throws Exception {
        // 是否弃用标记
        String coulmn = new ParameterIgnoreServiceImpl().getMarkKey(GodaddyCertificate.class);
        String[] markeValues = new ParameterIgnoreServiceImpl().getMarkedValues(GodaddyCertificate.class);
        HashMap<String, Object> filterParams = this.filterParamMarked(params, coulmn, markeValues);
        return new GodaddyCertificateDaoImpl().getCertificateTotal(filterParams);
    }

    // certificates
    public GodaddyCertificate getCertificate(int id) {
        return new GodaddyCertificateDaoImpl().getCertificate(id);
    }

    // certificates-查询所有证书
    public List<GodaddyCertificate> getCertificateList(HashMap<String, Object> params) throws Exception {
        // 是否弃用标记
        String coulmn = new ParameterIgnoreServiceImpl().getMarkKey(GodaddyCertificate.class);
        String[] markeValues = new ParameterIgnoreServiceImpl().getMarkedValues(GodaddyCertificate.class);
        HashMap<String, Object> filterParams = this.filterParamMarked(params, coulmn, markeValues);
        List<GodaddyCertificate> list = new GodaddyCertificateDaoImpl().getCertificateList(filterParams);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, Integer.parseInt(Parameter.alertThresholds.get("GODADDY_CERTIFICATE_EXPIRED_DAY")));
        Date thresholdDate = calendar.getTime();
        for (GodaddyCertificate gc : list) {
            if (gc.getCertificateStatus().equals("ISSUED")) {
                gc.setAlertExpired(gc.getValidEnd().before(thresholdDate));
            }
            if (ArrayUtils.hasString(markeValues, gc.getCertificateId())) {
                gc.setAlertMarked(true);
            }
            gc.setUserName(this.getUserName(gc.getAccessKeyId()));
        }
        return list;
    }

    // certificates-查询所有证书&分页
    public Map<String, Object> getCertificateList(HashMap<String, Object> params, int pageNumber, int pageSize) throws Exception {
        // 是否弃用标记
        String coulmn = new ParameterIgnoreServiceImpl().getMarkKey(GodaddyCertificate.class);
        String[] markeValues = new ParameterIgnoreServiceImpl().getMarkedValues(GodaddyCertificate.class);
        HashMap<String, Object> filterParams = this.filterParamMarked(params, coulmn, markeValues);
        List<GodaddyCertificate> list = new GodaddyCertificateDaoImpl().getCertificateList(filterParams, pageNumber, pageSize);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, Integer.parseInt(Parameter.alertThresholds.get("GODADDY_CERTIFICATE_EXPIRED_DAY")));
        Date thresholdDate = calendar.getTime();
        for (GodaddyCertificate gc : list) {
            if (gc.getCertificateStatus().equals("ISSUED")) {
                gc.setAlertExpired(gc.getValidEnd().before(thresholdDate));
            }
            if (ArrayUtils.hasString(markeValues, gc.getCertificateId())) {
                gc.setAlertMarked(true);
            }
            gc.setUserName(this.getUserName(gc.getAccessKeyId()));
        }
        // DATE为空转换失败所以用map
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("total", new GodaddyCertificateDaoImpl().getCertificateTotal(filterParams));
        result.put("items", list);
        return result;
    }

}
