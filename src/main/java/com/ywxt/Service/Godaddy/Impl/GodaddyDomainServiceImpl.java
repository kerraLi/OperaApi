package com.ywxt.Service.Godaddy.Impl;

import com.ywxt.Dao.Godaddy.Impl.GodaddyAccountDaoImpl;
import com.ywxt.Dao.Godaddy.Impl.GodaddyDomainDaoImpl;
import com.ywxt.Domain.Godaddy.GodaddyAccount;
import com.ywxt.Domain.Godaddy.GodaddyDomain;
import com.ywxt.Service.Godaddy.GodaddyDomainService;
import com.ywxt.Service.Impl.ParameterIgnoreServiceImpl;
import com.ywxt.Service.System.Impl.ParameterServiceImpl;
import com.ywxt.Utils.ArrayUtils;

import java.util.*;

public class GodaddyDomainServiceImpl extends GodaddyServiceImpl implements GodaddyDomainService {

    private String accessKeyId;
    private String accessKeySecret;
    private HashMap<String, String> userNameMap = new HashMap<>();

    public GodaddyDomainServiceImpl() {

    }

    public GodaddyDomainServiceImpl(String accessKeyId) throws Exception {
        GodaddyAccount godaddyAccount = new GodaddyAccountDaoImpl().getAccount(accessKeyId);
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = godaddyAccount.getAccessKeySecret();
    }

    public GodaddyDomainServiceImpl(String keyId, String keySecret) {
        this.accessKeyId = keyId;
        this.accessKeySecret = keySecret;
    }

    // 获取dash数据
    public HashMap<String, Object> getDashData() throws Exception {
        HashMap<String, Object> resultParams = new HashMap<String, Object>();
        // normal invalid
        HashMap<String, Object> params = new HashMap<String, Object>();
        for (Object[] os : new GodaddyDomainDaoImpl().getCountGroup(params)) {
            if (os[0].equals("ACTIVE")) {
                resultParams.put("go-domain-" + os[1] + "-normal", os[2]);
            } else {
                resultParams.put("go-domain-" + os[1] + "-invalid", os[2]);
            }
        }
        // expired
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, Integer.parseInt(new ParameterServiceImpl().getValue("GODADDY_DOMAIN_EXPIRED_DAY")));
        Date thresholdDate = calendar.getTime();
        params.put("status", "ACTIVE");
        params.put("expires@lt", thresholdDate);
        for (Object[] os : this.getDomainTotalByAccount(params)) {
            resultParams.put("go-domain-" + os[0] + "-expired", os[1]);
        }
        // deprecated
        params = new HashMap<String, Object>();
        params.put("ifMarked", "true");
        for (Object[] os : this.getDomainTotalByAccount(params)) {
            resultParams.put("go-domain-" + os[0] + "-deprecated", os[1]);
        }
        return resultParams;
    }

    // domain-获取个数按account分组
    public List<Object[]> getDomainTotalByAccount(HashMap<String, Object> params) throws Exception {
        // 是否弃用标记
        String coulmn = new ParameterIgnoreServiceImpl().getMarkKey(GodaddyDomain.class);
        String[] markeValues = new ParameterIgnoreServiceImpl().getMarkedValues(GodaddyDomain.class);
        HashMap<String, Object> filterParams = this.filterParamMarked(params, coulmn, markeValues);
        return new GodaddyDomainDaoImpl().getDomainTotalByAccount(filterParams);
    }

    // domain-获取个数
    public int getDomainTotal(HashMap<String, Object> params) throws Exception {
        // 是否弃用标记
        String coulmn = new ParameterIgnoreServiceImpl().getMarkKey(GodaddyDomain.class);
        String[] markeValues = new ParameterIgnoreServiceImpl().getMarkedValues(GodaddyDomain.class);
        HashMap<String, Object> filterParams = this.filterParamMarked(params, coulmn, markeValues);
        return new GodaddyDomainDaoImpl().getDomainTotal(filterParams);
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
        calendar.add(Calendar.DATE, Integer.parseInt(new ParameterServiceImpl().getValue("GODADDY_DOMAIN_EXPIRED_DAY")));
        Date thresholdDate = calendar.getTime();
        for (GodaddyDomain gd : list) {
            if (gd.getStatus().equals("ACTIVE")) {
                gd.setAlertExpired(gd.getExpires().before(thresholdDate));
            }
            if (ArrayUtils.hasString(markeValues, gd.getDomainId())) {
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
        calendar.add(Calendar.DATE, Integer.parseInt(new ParameterServiceImpl().getValue("GODADDY_DOMAIN_EXPIRED_DAY")));
        Date thresholdDate = calendar.getTime();
        for (GodaddyDomain gd : list) {
            if (gd.getStatus().equals("ACTIVE")) {
                gd.setAlertExpired(gd.getExpires().before(thresholdDate));
            }
            if (ArrayUtils.hasString(markeValues, gd.getDomainId())) {
                gd.setAlertMarked(true);
            }
            gd.setUserName(this.getUserName(gd.getAccessKeyId()));
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("total", new GodaddyDomainDaoImpl().getDomainTotal(filterParams));
        result.put("items", list);
        return result;
    }

}
