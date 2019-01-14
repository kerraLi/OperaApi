package com.ywxt.Service.Godaddy.Impl;

import com.ywxt.Dao.Ali.Impl.AliAccountDaoImpl;
import com.ywxt.Dao.Godaddy.GodaddyAccountDao;
import com.ywxt.Dao.Godaddy.GodaddyCertificateDao;
import com.ywxt.Dao.Godaddy.GodaddyDomainDao;
import com.ywxt.Dao.Godaddy.Impl.GodaddyAccountDaoImpl;
import com.ywxt.Dao.Godaddy.Impl.GodaddyCertificateDaoImpl;
import com.ywxt.Dao.Godaddy.Impl.GodaddyDomainDaoImpl;
import com.ywxt.Domain.Ali.AliAccount;
import com.ywxt.Domain.Godaddy.GodaddyAccount;
import com.ywxt.Domain.Godaddy.GodaddyCertificate;
import com.ywxt.Domain.Godaddy.GodaddyDomain;
import com.ywxt.Handler.PropertyStrategyHandler;
import com.ywxt.Service.Godaddy.GodaddyService;
import com.ywxt.Service.Impl.ParameterIgnoreServiceImpl;
import com.ywxt.Utils.HttpUtils;
import com.ywxt.Utils.Parameter;
import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.JSONUtils;
import net.sf.json.util.PropertySetStrategy;

import java.util.*;

public class GodaddyServiceImpl implements GodaddyService {

    private String accessKeyId;
    private String accessKeySecret;
    private HashMap<String, String> userNameMap = new HashMap<>();


    public GodaddyServiceImpl() {

    }

    public GodaddyServiceImpl(String accessKeyId) throws Exception {
        GodaddyAccount godaddyAccount = new GodaddyAccountDaoImpl().getAccount(accessKeyId);
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = godaddyAccount.getAccessKeySecret();
    }

    public GodaddyServiceImpl(String keyId, String keySecret) {
        this.accessKeyId = keyId;
        this.accessKeySecret = keySecret;
    }

    // 获取数据
    private String getData(String action, String paramContext) throws Exception {
        Map<String, String> headerParams = new HashMap<String, String>();
        headerParams.put("Authorization", "sso-key " + this.accessKeyId + ":" + this.accessKeySecret);
        headerParams.put("Content-type", "application/json");
        return HttpUtils.sendConnGet(Parameter.godaddyUrl + Parameter.godaddyActions.get(action), paramContext, headerParams);
    }

    // 更新源数据
    public void freshSourceData() throws Exception {
        this.freshDomain();
        this.freshCertificate();
    }

    // 更新域名数据
    public void freshDomain() throws Exception {
        new GodaddyDomainDaoImpl().deleteDomainByAccessId(this.accessKeyId);
        List<GodaddyDomain> gdList = new ArrayList<>();
        int pageSize = 100;
        // marker:"前一次查询最后一条数据"
        String markerDomain = "";
        while (true) {
            HashMap<String, String> inParam = new HashMap<>();
            inParam.put("limit", String.valueOf(pageSize));
            inParam.put("marker", markerDomain);
            String paramContext = HttpUtils.getParamContext(inParam);
            JSONArray result = JSONArray.fromObject(this.getData("GET_DOMAIN_LIST", paramContext));
            for (int i = 0; i < result.size(); i++) {
                JSONObject object = (JSONObject) result.get(i);
                object.put("accessKeyId", this.accessKeyId);
                object.put("createdAt", object.getString("createdAt").replace("Z", " UTC"));
                object.put("expires", object.getString("expires").replace("Z", " UTC"));
                if (object.has("renewDeadline")) {
                    object.put("renewDeadline", object.getString("renewDeadline").replace("Z", " UTC"));
                }
                JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[]{"yyyy-MM-dd"}));
                gdList.add((GodaddyDomain) JSONObject.toBean(object, GodaddyDomain.class));
                if (i == (result.size() - 1)) {
                    markerDomain = object.getString("domain");
                }
            }
            // 最后一页 跳出
            if (result.size() < pageSize) {
                break;
            }
        }
        new GodaddyDomainDaoImpl().saveDomains(gdList);
    }

    // 更新证书
    public void freshCertificate() throws Exception {
        new GodaddyCertificateDaoImpl().deleteCertificateByAccessId(this.accessKeyId);
        String paramContext = "";
        List<GodaddyCertificate> gcList = new ArrayList<>();
        JSONArray result = JSONArray.fromObject(this.getData("GET_CERTIFICATE_LIST", paramContext));
        for (int i = 0; i < result.size(); i++) {
            JSONObject object = (JSONObject) result.get(i);
            object.put("accessKeyId", this.accessKeyId);
            // 过滤字段
            JsonConfig config = new JsonConfig();
            // 设置属性包装器
            config.setPropertySetStrategy(new PropertyStrategyHandler(PropertySetStrategy.DEFAULT));
            // 设置要转换成的JavaBean
            config.setRootClass(GodaddyCertificate.class);
            // 日期转换
            JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[]{"yyyy-MM-dd HH:mm:ss"}));
            gcList.add((GodaddyCertificate) JSONObject.toBean(object, config));
        }
        new GodaddyCertificateDaoImpl().saveCertificates(gcList);
    }


    // domain
    public GodaddyDomain getDomain(int id) {
        return new GodaddyDomainDaoImpl().getDomain(id);
    }

    // domain-查询所有域名
    public List<GodaddyDomain> getDomainList(HashMap<String, Object> params) throws Exception {
        // 是否弃用标记
        String coulmn = new ParameterIgnoreServiceImpl().getMarkKey(GodaddyDomain.class);
        String[] markeValues = new ParameterIgnoreServiceImpl().getMarkedValues(GodaddyDomain.class);
        HashMap<String, Object> filterParams = this.filterParamMarked(params, coulmn, markeValues);
        List<GodaddyDomain> list = new GodaddyDomainDaoImpl().getDomainList(filterParams);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, Integer.parseInt(Parameter.alertThresholds.get("GODADDY_DOMAIN_EXPIRED_DAY")));
        Date thresholdDate = calendar.getTime();
        for (GodaddyDomain gd : list) {
            if (gd.getStatus().equals("ACTIVE")) {
                gd.setAlertExpired(gd.getExpires().before(thresholdDate));
            }
            if (Arrays.binarySearch(markeValues, gd.getDomainId()) >= 0) {
                gd.setAlertMarked(true);
            }
            gd.setUserName(this.getUserName(gd.getAccessKeyId()));
        }
        return list;
    }

    // domain-查询所有域名&分页
    public Map<String, Object> getDomainList(HashMap<String, Object> params, int pageNumber, int pageSize) throws Exception {
        // 是否弃用标记
        String coulmn = new ParameterIgnoreServiceImpl().getMarkKey(GodaddyDomain.class);
        String[] markeValues = new ParameterIgnoreServiceImpl().getMarkedValues(GodaddyDomain.class);
        HashMap<String, Object> filterParams = this.filterParamMarked(params, coulmn, markeValues);
        List<GodaddyDomain> list = new GodaddyDomainDaoImpl().getDomainList(filterParams, pageNumber, pageSize);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, Integer.parseInt(Parameter.alertThresholds.get("GODADDY_DOMAIN_EXPIRED_DAY")));
        Date thresholdDate = calendar.getTime();
        for (GodaddyDomain gd : list) {
            if (gd.getStatus().equals("ACTIVE")) {
                gd.setAlertExpired(gd.getExpires().before(thresholdDate));
            }
            if (Arrays.binarySearch(markeValues, gd.getDomainId()) >= 0) {
                gd.setAlertMarked(true);
            }
            gd.setUserName(this.getUserName(gd.getAccessKeyId()));
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("total", new GodaddyDomainDaoImpl().getDomainTotal(filterParams));
        result.put("items", list);
        return result;
    }

    // domain
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
            if (Arrays.binarySearch(markeValues, gc.getCertificateId()) >= 0) {
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
            if (Arrays.binarySearch(markeValues, gc.getCertificateId()) >= 0) {
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

    // 过滤弃用param
    private HashMap<String, Object> filterParamMarked(HashMap<String, Object> params, String coulmn, String[] markeValues) {
        boolean ifMarked = (params.get("ifMarked") != null) && (params.get("ifMarked").equals("true"));
        if (ifMarked) {
            if (markeValues.length > 0) {
                params.put(coulmn + "@in", markeValues);
            } else {
                params.put(coulmn + "@eq", "");
            }
        } else {
            if (markeValues.length > 0) {
                params.put(coulmn + "@notIn", markeValues);
            }
        }
        params.remove("ifMarked");
        return params;
    }

    // 获取userName
    private String getUserName(String accessKeyId) throws Exception {
        if (this.userNameMap.get(accessKeyId) == null) {
            GodaddyAccount godaddyAccount = new GodaddyAccountDaoImpl().getAccount(accessKeyId);
            this.userNameMap.put(accessKeyId, godaddyAccount.getUserName());
            return godaddyAccount.getUserName();
        } else {
            return this.userNameMap.get(accessKeyId);
        }
    }

}
