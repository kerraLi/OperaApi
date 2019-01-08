package com.ywxt.Controller.Ali;

import com.aliyuncs.bssopenapi.model.v20171214.QueryAccountBalanceResponse;
import com.aliyuncs.cdn.model.v20141111.DescribeRefreshTasksResponse;
import com.aliyuncs.cdn.model.v20141111.DescribeUserDomainsResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import com.ywxt.Domain.AliAccount;
import com.ywxt.Domain.AliCdn;
import com.ywxt.Domain.AliEcs;
import com.ywxt.Service.Ali.Impl.AliServiceImpl;
import com.ywxt.Utils.Parameter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/ali")
public class AliController {

    // 账号列表
    @ResponseBody
    @RequestMapping(value = {"/account/list"}, method = RequestMethod.POST)
    public List<AliAccount> accountList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<AliAccount> aaList = new ArrayList<>();
        for (Map.Entry<String, String> e : Parameter.aliAccounts.entrySet()) {
            AliAccount aa = new AliAccount(e.getKey(), e.getValue());
            QueryAccountBalanceResponse.Data data = new AliServiceImpl(e.getKey(), e.getValue()).getAccountBalance();
            // ali 金额 带千分符(,)
            if (new DecimalFormat().parse(data.getAvailableAmount()).doubleValue() <= Double.parseDouble(Parameter.alertThresholds.get("ALI_ACCOUNT_BALANCE"))) {
                aa.setAlertBalance(true);
            }
            aa.setBalanceData(data);
            aaList.add(aa);
        }
        return aaList;
    }

    // ecs:服务器列表
    @ResponseBody
    @RequestMapping(value = {"/ecs/list"}, method = RequestMethod.POST)
    public List<AliEcs> ecsList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<AliEcs> diList = new ArrayList<>();
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm Z");
//        df.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.DATE, Integer.parseInt(Parameter.alertThresholds.get("ALI_ECS_EXPIRED_DAY")));
//        Date thresholdDate = calendar.getTime();
//        for (Map.Entry<String, String> e : Parameter.aliAccounts.entrySet()) {
//            for (DescribeInstancesResponse.Instance instance : new AliServiceImpl(e.getKey(), e.getValue()).getEcsList()) {
//                AliEcs ae = new AliEcs(instance);
//                ae.setAccessKeyId(e.getKey());
//                Date expiredTime = df.parse(ae.getExpiredTime().replace("Z", " UTC"));
//                ae.setAlertExpired(expiredTime.before(thresholdDate));
//                // System.out.println(expiredTime);
//                // System.out.println(nowDate);
//                diList.add(ae);
//            }
//        }
        return diList;
    }

    // cdn:cdn域名列表
    @ResponseBody
    @RequestMapping(value = {"/cdn/domain/list"}, method = RequestMethod.POST)
    public List<AliCdn> cdnDomainList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<AliCdn> domainList = new ArrayList<>();
        for (Map.Entry<String, String> e : Parameter.aliAccounts.entrySet()) {
            for (DescribeUserDomainsResponse.PageData pageData : new AliServiceImpl(e.getKey(), e.getValue()).getCdnDomainList()) {
                AliCdn ac = new AliCdn(pageData);
                ac.setAccessKeyId(e.getKey());
                domainList.add(ac);
            }
        }
        return domainList;
    }


    // cdn:节点刷新
    @ResponseBody
    @RequestMapping(value = {"/cdn/refresh"}, method = RequestMethod.POST)
    public Map<String, String> cdnRefreshObjectCache(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String accessId = request.getParameter("access-id");
        String objectPath = request.getParameter("object-path");
        String objectType = request.getParameter("object-type");
        if (objectType.isEmpty()) {
            objectType = "File";
        }
        return new AliServiceImpl(accessId, Parameter.aliAccounts.get(accessId)).refreshCdnObjectCaches(objectPath, objectType);
    }

    // cdn:节点刷新任务查看
    @ResponseBody
    @RequestMapping(value = {"/cdn/refresh/task"}, method = RequestMethod.POST)
    public DescribeRefreshTasksResponse.CDNTask getRefreshObjectCacheTask(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String accessId = request.getParameter("access-id");
        String taskId = request.getParameter("task-id");
        return new AliServiceImpl(accessId, Parameter.aliAccounts.get(accessId)).getCdnRefreshTask(taskId).get(0);
    }
}
