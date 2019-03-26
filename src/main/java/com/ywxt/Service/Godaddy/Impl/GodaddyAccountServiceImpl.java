package com.ywxt.Service.Godaddy.Impl;

import com.alibaba.fastjson.JSONArray;
import com.ywxt.Dao.Godaddy.GodaddyAccountDao;
import com.ywxt.Domain.Godaddy.GodaddyAccount;
import com.ywxt.Handler.AsyncHandler;
import com.ywxt.Service.Godaddy.GodaddyAccountService;
import com.ywxt.Service.Godaddy.GodaddyService;
import com.ywxt.Utils.AsyncUtils;
import com.ywxt.Utils.HttpUtils;
import com.ywxt.Utils.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GodaddyAccountServiceImpl implements GodaddyAccountService {

    @Autowired
    private GodaddyAccountDao godaddyAccountDao;
    @Autowired
    private GodaddyService godaddyService;

    // 列表
    public List<GodaddyAccount> getList() {
        return godaddyAccountDao.findAll();
    }

    // 列表&设置密钥权限
    public List<GodaddyAccount> getList(boolean isSpecialPermission) {
        List<GodaddyAccount> list = godaddyAccountDao.findAll();
        for (GodaddyAccount ga : list) {
            // 设置密钥权限
            ga.setIsHiddenSecrete(isSpecialPermission);
            if (isSpecialPermission) {
                ga.setAccessKeySecret(null);
            }
        }
        return list;
    }

    // 新增/修改
    public GodaddyAccount saveAccount(GodaddyAccount godaddyAccount) throws Exception {
        // check key
        if (this.checkAccount(godaddyAccount.getAccessKeyId(), godaddyAccount.getAccessKeySecret())) {
            godaddyAccount.setStatus("normal");
            // update Data & 异步
            AsyncHandler handler = new AsyncHandler() {
                @Override
                public void handle() {
                    try {
                        godaddyService.freshSourceData(godaddyAccount.getAccessKeyId(), godaddyAccount.getAccessKeySecret());
                    } catch (Exception e) {
                        // 异步处理数据错误
                        System.out.println(e.getMessage());
                    }
                }
            };
            AsyncUtils.asyncWork(handler);
        } else {
            godaddyAccount.setStatus("invalid");
        }
        // 单独处理password
        if (godaddyAccount.getId() != 0 && godaddyAccount.getAccessKeySecret() == null) {
            GodaddyAccount oldAccount = godaddyAccountDao.findGodaddyAccountById(godaddyAccount.getId());
            godaddyAccount.setAccessKeySecret(oldAccount.getAccessKeySecret());
        }
        return godaddyAccountDao.saveAndFlush(godaddyAccount);
    }

    // 删除账号
    public void deleteAccount(int id) {
        // update Data
        GodaddyAccount godaddyAccount = godaddyAccountDao.findGodaddyAccountById(id);
        if (godaddyAccount.getStatus().equals("normal")) {
            // update Data & 异步
            AsyncHandler handler = new AsyncHandler() {
                @Override
                public void handle() {
                    godaddyService.removeSourceData(godaddyAccount.getAccessKeyId());
                }
            };
            AsyncUtils.asyncWork(handler);
        }
        godaddyAccountDao.deleteById(id);
    }

    // 校验密钥
    private boolean checkAccount(String accessKeyId, String accessKeySecret) throws Exception {
        try {
            HashMap<String, String> inParam = new HashMap<>();
            inParam.put("limit", "1");
            String paramContext = HttpUtils.getParamContext(inParam);
            Map<String, String> headerParams = new HashMap<String, String>();
            headerParams.put("Authorization", "sso-key " + accessKeyId + ":" + accessKeySecret);
            headerParams.put("Content-type", "application/json");
            JSONArray array = JSONArray.parseArray(HttpUtils.sendConnGet(Parameter.godaddyUrl + Parameter.godaddyActions.get("GET_DOMAIN_LIST"), paramContext, headerParams));
        } catch (IOException e) {
            if (e.getMessage().equals("Unauthorized")) {
                return false;
            }
        }
        return true;
    }

}
