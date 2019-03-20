package com.ywxt.Service.Godaddy.Impl;

import com.ywxt.Dao.Godaddy.Impl.GodaddyAccountDaoImpl;
import com.ywxt.Dao.Godaddy.Impl.GodaddyCertificateDaoImpl;
import com.ywxt.Domain.Godaddy.GodaddyAccount;
import com.ywxt.Domain.Godaddy.GodaddyCertificate;
import com.ywxt.Service.Godaddy.GodaddyCertificateService;
import com.ywxt.Service.System.Impl.IgnoreServiceImpl;
import com.ywxt.Service.System.Impl.ParameterServiceImpl;
import com.ywxt.Utils.ArrayUtils;

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
        HashMap<String, Object> resultParams = new HashMap<String, Object>();
        // normal invalid
        HashMap<String, Object> params = new HashMap<String, Object>();
        for (Object[] os : new GodaddyCertificateDaoImpl().getCountGroup(params)) {
            if (os[0].equals("ISSUED")) {
                resultParams.put("go-certificate-" + os[1] + "-normal", os[2]);
            } else {
                resultParams.put("go-certificate-" + os[1] + "-invalid", os[2]);
            }
        }
        // expired
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, Integer.parseInt(new ParameterServiceImpl().getValue("GODADDY_CERTIFICATE_EXPIRED_DAY")));
        Date thresholdDate = calendar.getTime();
        params.put("certificateStatus", "ISSUED");
        params.put("validEnd@lt", thresholdDate);
        for (Object[] os : this.getCertificateTotalByAccount(params)) {
            resultParams.put("go-certificate-" + os[0] + "-expired", os[1]);
        }
        // deprecated
        params = new HashMap<String, Object>();
        params.put("ifMarked", "true");
        for (Object[] os : this.getCertificateTotalByAccount(params)) {
            resultParams.put("go-certificate-" + os[0] + "-deprecated", os[1]);
        }
        return resultParams;
    }

    // certificates-获取个数按account分组
    public List<Object[]> getCertificateTotalByAccount(HashMap<String, Object> params) throws Exception {
        // 是否弃用标记
        //String coulmn = new IgnoreServiceImpl().getMarkKey(GodaddyCertificate.class);
        //String[] markeValues = new IgnoreServiceImpl().getMarkedValues(GodaddyCertificate.class);
        //HashMap<String, Object> filterParams = this.filterParamMarked(params, coulmn, markeValues);
        return new GodaddyCertificateDaoImpl().getCertificateTotalByAccount(params);
    }

    // certificates-获取个数
    public int getCertificateTotal(HashMap<String, Object> params) throws Exception {
        // 是否弃用标记
        //String coulmn = new IgnoreServiceImpl().getMarkKey(GodaddyCertificate.class);
        //String[] markeValues = new IgnoreServiceImpl().getMarkedValues(GodaddyCertificate.class);
        //HashMap<String, Object> filterParams = this.filterParamMarked(params, coulmn, markeValues);
        return new GodaddyCertificateDaoImpl().getCertificateTotal(params);
    }

    // certificates
    public GodaddyCertificate getCertificate(int id) {
        return new GodaddyCertificateDaoImpl().getCertificate(id);
    }

    // certificates-查询所有证书
    public List<GodaddyCertificate> getCertificateList(HashMap<String, Object> params) throws Exception {
        // 是否弃用标记
        //String coulmn = new IgnoreServiceImpl().getMarkKey(GodaddyCertificate.class);
        //String[] markeValues = new IgnoreServiceImpl().getMarkedValues(GodaddyCertificate.class);
        // HashMap<String, Object> filterParams = this.filterParamMarked(params, coulmn, markeValues);
        List<GodaddyCertificate> list = new GodaddyCertificateDaoImpl().getCertificateList(params);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, Integer.parseInt(new ParameterServiceImpl().getValue("GODADDY_CERTIFICATE_EXPIRED_DAY")));
        Date thresholdDate = calendar.getTime();
        for (GodaddyCertificate gc : list) {
            if (gc.getCertificateStatus().equals("ISSUED")) {
                gc.setAlertExpired(gc.getValidEnd().before(thresholdDate));
            }
            //if (ArrayUtils.hasString(markeValues, gc.getCertificateId())) {
            //    gc.setAlertMarked(true);
            //}
            gc.setUserName(this.getUserName(gc.getAccessKeyId()));
        }
        return list;
    }

    // certificates-查询所有证书&分页
    public Map<String, Object> getCertificateList(HashMap<String, Object> params, int pageNumber, int pageSize) throws Exception {
        // 是否弃用标记
        //String coulmn = new IgnoreServiceImpl().getMarkKey(GodaddyCertificate.class);
        //String[] markeValues = new IgnoreServiceImpl().getMarkedValues(GodaddyCertificate.class);
        //HashMap<String, Object> filterParams = this.filterParamMarked(params, coulmn, markeValues);
        List<GodaddyCertificate> list = new GodaddyCertificateDaoImpl().getCertificateList(params, pageNumber, pageSize);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, Integer.parseInt(new ParameterServiceImpl().getValue("GODADDY_CERTIFICATE_EXPIRED_DAY")));
        Date thresholdDate = calendar.getTime();
        for (GodaddyCertificate gc : list) {
            if (gc.getCertificateStatus().equals("ISSUED")) {
                gc.setAlertExpired(gc.getValidEnd().before(thresholdDate));
            }
            //if (ArrayUtils.hasString(markeValues, gc.getCertificateId())) {
            //    gc.setAlertMarked(true);
            //}
            gc.setUserName(this.getUserName(gc.getAccessKeyId()));
        }
        // DATE为空转换失败所以用map
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("total", new GodaddyCertificateDaoImpl().getCertificateTotal(params));
        result.put("items", list);
        return result;
    }

}
