package com.ywxt.Command;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Dao.Ali.Impl.AliAccountDaoImpl;
import com.ywxt.Dao.Godaddy.Impl.GodaddyAccountDaoImpl;
import com.ywxt.Dao.Impl.ParameterDaoImpl;
import com.ywxt.Dao.Impl.UserDaoImpl;
import com.ywxt.Dao.LogOperationDao;
import com.ywxt.Domain.Ali.AliAccount;
import com.ywxt.Domain.Ali.AliEcs;
import com.ywxt.Domain.Godaddy.GodaddyAccount;
import com.ywxt.Domain.Godaddy.GodaddyCertificate;
import com.ywxt.Domain.Role;
import com.ywxt.Domain.LogOperation;
import com.ywxt.Domain.Resource.Hardware;
import com.ywxt.Domain.User;
import com.ywxt.Service.Ali.Impl.AliAccountServiceImpl;
import com.ywxt.Service.Ali.Impl.AliEcsServiceImpl;
import com.ywxt.Service.Ali.Impl.AliServiceImpl;
import com.ywxt.Service.Aws.Impl.AwsAccountServiceImpl;
import com.ywxt.Service.Aws.Impl.AwsServiceImpl;
import com.ywxt.Service.Godaddy.Impl.GodaddyAccountServiceImpl;
import com.ywxt.Service.Godaddy.Impl.GodaddyServiceImpl;
import com.ywxt.Service.Impl.MessageServiceImpl;
import com.ywxt.Service.Impl.ParameterIgnoreServiceImpl;
import com.ywxt.Service.Impl.UserServiceImpl;
import com.ywxt.Service.Resource.Impl.HardwareServiceImpl;
import com.ywxt.Utils.MD5Utils;
import com.ywxt.Utils.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class TestCommand {

    @Resource
    private LogOperationDao logOperationDao;
    @Resource
    private TestCommand testCommand;

    private static void saveAliAccount() throws Exception {
        AliAccount aa = new AliAccount();
        aa.setUserName("asdbbbbbbbb");
        aa.setAccessKeyId("123aaaaaaaaa");
        aa.setAccessKeySecret("154");
        aa.setStatus("normal");
        new AliAccountServiceImpl().saveAliAccount(aa);
    }

    private static void saveParameter() throws Exception {
        com.ywxt.Domain.Parameter parameter = new com.ywxt.Domain.Parameter();
        parameter.setKey("ALI_ACCOUNT_BALANCE");
        parameter.setValue("3000.00");
        parameter.setStatus("fixed");
        new ParameterDaoImpl().save(parameter);
        com.ywxt.Domain.Parameter parameter2 = new com.ywxt.Domain.Parameter();
        parameter2.setKey("ALI_ECS_EXPIRED_DAY");
        parameter2.setValue("3");
        parameter2.setStatus("fixed");
        new ParameterDaoImpl().save(parameter2);
        com.ywxt.Domain.Parameter parameter3 = new com.ywxt.Domain.Parameter();
        parameter3.setKey("GODADDY_DOMAIN_EXPIRED_DAY");
        parameter3.setValue("30");
        parameter3.setStatus("fixed");
        new ParameterDaoImpl().save(parameter3);
        com.ywxt.Domain.Parameter parameter4 = new com.ywxt.Domain.Parameter();
        parameter4.setKey("GODADDY_CERTIFICATE_EXPIRED_DAY");
        parameter4.setValue("30");
        parameter4.setStatus("fixed");
        new ParameterDaoImpl().save(parameter4);
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
        Role role = new Role();
       /* String[] roles = {"admin"};
        u.setRoles(roles);*/
        userDao.saveUser(u);
    }

    private static void readUser() {
        // 读取用户
        User user = new UserServiceImpl().getUserById(1);

        for (Role s : user.getRoles()) {
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
//        List<AliEcsController> list = new AliServiceImpl("instanceId", "i-j6cenf2ak8gsbem4lpps").getEcsList(new HashMap<String, Object>() {{
//            put("instanceId", "i-j6cenf2ak8gsbem4lpps");
//        }}, 1, 10);
//        for (AliEcsController ae : list) {
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

    private static void setIgnore() throws Exception {
        AliEcs aliEcs = new AliEcs();
        aliEcs.setInstanceId("aaaaaaaaaa");
//        new ParameterIgnoreServiceImpl().saveMarked(aliEcs);
//        boolean a = new ParameterIgnoreServiceImpl().checkIfMarked(aliEcs);
        new ParameterIgnoreServiceImpl().deleteMarked(aliEcs);
//        System.out.println(a);
    }

    private static void checkAws() throws Exception {
        //new AwsAccountServiceImpl().checkAccount("AKIAJB2HURSIKSOB67ZQ", "No4K6AlQvR+V43j732g+UQ7QygRyOXWSCxb7qVP6");
        new AwsServiceImpl("AKIAJB2HURSIKSOB67ZQ", "No4K6AlQvR+V43j732g+UQ7QygRyOXWSCxb7qVP6").freshEc2();
    }

    // 更新单个ecs
    private static void updateEcs() throws Exception {
        new AliEcsServiceImpl().updateEcs("i-j6c56n9npzzm3mnq6a3d");
    }

    // log operation jpa方式
    private void saveLog() throws Exception {
        LogOperation logOperation = new LogOperation();
        logOperation.setRequestId("test123");
        logOperationDao.create(logOperation);
    }

    public static void main(String[] args) throws Exception {
        System.out.println(123);
        System.out.println("发送消息");
        String test = "a|b|c|";
        test = test.substring(0, test.length() - 1);

        System.out.println(test);
//        TestCommand.updateEcs();
//        TestCommand testCommand = new TestCommand();
//        testCommand.saveLog();
    }
}
