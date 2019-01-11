package com.ywxt.Command;

import com.ywxt.Dao.Ali.Impl.AliAccountDaoImpl;
import com.ywxt.Dao.Godaddy.Impl.GodaddyAccountDaoImpl;
import com.ywxt.Dao.Impl.UserDaoImpl;
import com.ywxt.Domain.Ali.AliAccount;
import com.ywxt.Domain.Ali.AliEcs;
import com.ywxt.Domain.Godaddy.GodaddyAccount;
import com.ywxt.Domain.Godaddy.GodaddyCertificate;
import com.ywxt.Domain.User;
import com.ywxt.Service.Ali.Impl.AliAccountServiceImpl;
import com.ywxt.Service.Ali.Impl.AliServiceImpl;
import com.ywxt.Service.Godaddy.Impl.GodaddyAccountServiceImpl;
import com.ywxt.Service.Godaddy.Impl.GodaddyServiceImpl;
import com.ywxt.Service.Impl.ParameterIgnoreServiceImpl;
import com.ywxt.Service.Impl.UserServiceImpl;
import com.ywxt.Utils.MD5Utils;
import com.ywxt.Utils.Parameter;
import net.sf.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class TestCommand {

    private static void saveAliAccount() throws Exception {
        AliAccount aa = new AliAccount();
        aa.setUserName("asdbbbbbbbb");
        aa.setAccessKeyId("123aaaaaaaaa");
        aa.setAccessKeySecret("154");
        aa.setStatus("normal");
        new AliAccountServiceImpl().saveAliAccount(aa);
    }

    private static void saveAdmin() {
        UserDaoImpl userDao = new UserDaoImpl();
        // 创建用户
        User u = new User();
        u.setUsername("admin");
        u.setPassword(MD5Utils.md5("123456"));
        u.setNickname("超级管理员");
        u.setAvatar("https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        u.setIntroduction("我是超级管理员");
        String[] roles = {"admin"};
        u.setRoles(roles);
        userDao.saveUser(u);
    }

    private static void readUser() {
        // 读取用户
        User user = new UserServiceImpl().getUserById(1);
        for (String s : user.getRoles()) {
            System.out.println(s);
        }
        System.out.println();
        System.out.println(user.getUsername());
    }

    // 校验ali账户
    private static boolean checkAccount() throws Exception {
        return new AliAccountServiceImpl().checkAccount("LTAIDmBAC9rB3SlR", "C2FD6tBp8r8jE7PvtTJsi63IDZ4tFE");
    }

    // 更新ali数据
    private static void refreshAli() throws Exception {
        new AliServiceImpl("LTAIDmBAC9rB3SlR", "C2FD6tBp8r8jE7PvtTJsi63IDZ4tFE").freshSourceData();
    }

    // 数据库查ecs数据
    private static void getAliEcs() throws Exception {
//        List<AliEcs> list = new AliServiceImpl("instanceId", "i-j6cenf2ak8gsbem4lpps").getEcsList(new HashMap<String, Object>() {{
//            put("instanceId", "i-j6cenf2ak8gsbem4lpps");
//        }}, 1, 10);
//        for (AliEcs ae : list) {
//            System.out.println(ae.getInstanceId());
//            System.out.println(ae.getAlertExpired());
//            System.out.println(ae.toString());
//        }
    }

    private static void setGodaddyAccount() throws Exception {
        GodaddyAccount account = new GodaddyAccount();
        account.setUserName("yingmu007");
        account.setAccessKeyId("dLDHSMnAAEN6_E1XDuVUJEhuwSpvrcCA59t");
        account.setAccessKeySecret("E1axtvaUTwMNrQnzFoSFY6");
        new GodaddyAccountServiceImpl().saveAccount(account);
        System.out.println("跑完了");
    }

    private static void checkGodaddyAccount() throws Exception {
//        new GodaddyAccountServiceImpl().checkAccount("aasd","asd");

//        HashMap<String, Object> params = new HashMap<String, Object>();
//        JSONObject jsonObject = new GodaddyServiceImpl().getCertificateList(params, 1, 20);
//        System.out.println(jsonObject.get("total"));
//        for (Object o : (List) jsonObject.get("items")) {
//            System.out.println(o.toString());
//        }
    }

    // normal account
    private static void getNormalAccount() {
        List<AliAccount> list = new AliAccountDaoImpl().getAliAccountsNormal();
        for (AliAccount ae : list) {
            System.out.println(ae.getAccessKeyId());
        }
    }

    private static void setIgnore() throws Exception{
        AliEcs aliEcs = new AliEcs();
        aliEcs.setInstanceId("aaaaaaaaaa");
//        new ParameterIgnoreServiceImpl().saveMarked(aliEcs);
//        boolean a = new ParameterIgnoreServiceImpl().checkIfMarked(aliEcs);
        new ParameterIgnoreServiceImpl().deleteMarked(aliEcs);
//        System.out.println(a);
    }

    public static void main(String[] args) throws Exception {
        System.out.println(123);

//        TestCommand.setIgnore();
//        TestCommand.checkGodaddyAccount();
//        TestCommand.setGodaddyAccount();
//        TestCommand.checkGodaddyAccount();

//        TestCommand.saveAliAccount();
//        TestCommand.getNormalAccount();
//        TestCommand.getAliEcs();
//        TestCommand.refreshAli();
//        System.out.println(TestCommand.checkAccount());
        TestCommand.saveAdmin();
//        TestCommand.saveAliAccount();

    }
}
