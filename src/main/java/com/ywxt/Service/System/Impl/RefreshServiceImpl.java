package com.ywxt.Service.System.Impl;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Dao.Log.Impl.LogRefreshDaoImpl;
import com.ywxt.Dao.Log.LogRefreshDao;
import com.ywxt.Domain.Ali.AliAccount;
import com.ywxt.Domain.Aws.AwsAccount;
import com.ywxt.Domain.Godaddy.GodaddyAccount;
import com.ywxt.Domain.Log.LogRefresh;
import com.ywxt.Handler.AsyncHandler;
import com.ywxt.Service.Ali.Impl.AliAccountServiceImpl;
import com.ywxt.Service.Ali.Impl.AliServiceImpl;
import com.ywxt.Service.Aws.Impl.AwsAccountServiceImpl;
import com.ywxt.Service.Aws.Impl.AwsServiceImpl;
import com.ywxt.Service.Godaddy.Impl.GodaddyAccountServiceImpl;
import com.ywxt.Service.Godaddy.Impl.GodaddyServiceImpl;
import com.ywxt.Service.System.RefreshService;
import com.ywxt.Utils.AsyncUtils;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class RefreshServiceImpl implements RefreshService {
    @Autowired
    private ObjectFactory<AliServiceImpl> objectFactory;

    // 获取刷新类型
    public List<JSONObject> refreshTypes() {
        String[] types = new String[]{"ali", "aws", "godaddy"};
        List<JSONObject> list = new ArrayList<>();
        for (String type : types) {
            LogRefresh last = new LogRefreshDaoImpl().getLast(type);
            JSONObject temp = new JSONObject();
            temp.put("type", type);
            temp.put("name", type.toUpperCase());
            temp.put("last_date", last == null ? 0 : last.getTime());
            list.add(temp);
        }
        return list;
    }

    // 数据刷新
    public void refreshData(String type) {
        AsyncHandler handler = new AsyncHandler() {
            @Override
            public void handle() {
                try {
                    switch (type) {
                        case "ali":
                            List<AliAccount> aliAccounts = new AliAccountServiceImpl().getList();
                            for (AliAccount aliAccount : aliAccounts) {
                                if (aliAccount.getStatus().equals("normal")) {
                                    objectFactory.getObject().freshSourceData(aliAccount.getAccessKeyId(),aliAccount.getAccessKeySecret());
                                }
                            }
                            break;
                        case "aws":
                            List<AwsAccount> awsAccounts = new AwsAccountServiceImpl().getList();
                            for (AwsAccount awsAccount : awsAccounts) {
                                if (awsAccount.getStatus().equals("normal")) {
                                    new AwsServiceImpl(awsAccount.getAccessKeyId(), awsAccount.getAccessKeySecret()).freshSourceData();
                                }
                            }
                            break;
                        case "go":
                            List<GodaddyAccount> goAccounts = new GodaddyAccountServiceImpl().getList();
                            for (GodaddyAccount godaddyAccount : goAccounts) {
                                if (godaddyAccount.getStatus().equals("normal")) {
                                    new GodaddyServiceImpl(godaddyAccount.getAccessKeyId(), godaddyAccount.getAccessKeySecret()).freshSourceData();
                                }
                            }
                            break;
                        default:
                            break;
                    }
                    // 记录更新时间
                    LogRefresh log = new LogRefresh();
                    log.setTime(new Date());
                    log.setType(type);
                    new RefreshServiceImpl().saveRefreshLog(log);
                } catch (Exception e) {
                    // 异步处理数据错误
                    System.out.println(e.getMessage());
                }
            }
        };
        AsyncUtils.asyncWork(handler);
    }

    // 保存刷新日志
    public void saveRefreshLog(LogRefresh logRefresh) {
        new LogRefreshDaoImpl().saveLogRefresh(logRefresh);
    }


}
