package com.ywxt.Service.Aws.Impl;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Dao.Aws.Impl.AwsAccountDaoImpl;
import com.ywxt.Dao.Aws.Impl.AwsEc2DaoImpl;
import com.ywxt.Domain.Aws.AwsAccount;
import com.ywxt.Domain.Aws.AwsEc2;

import java.util.HashMap;
import java.util.List;

public class AwsEc2ServiceImpl extends AwsServiceImpl {
    private String accessKeyId;
    private String accessKeySecret;
    private HashMap<String, String> userNameMap = new HashMap<>();

    public AwsEc2ServiceImpl() {
    }

    public AwsEc2ServiceImpl(String accessKeyId) throws Exception {
        AwsAccount awsAccount = new AwsAccountDaoImpl().getAccount(accessKeyId);
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = awsAccount.getAccessKeySecret();
    }

    public AwsEc2ServiceImpl(String keyId, String keySecret) {
        this.accessKeyId = keyId;
        this.accessKeySecret = keySecret;
    }

    // ec2-获取个数
    public int getEc2Total(HashMap<String, Object> params) throws Exception {
        return new AwsEc2DaoImpl().getTotal(params);
    }

    // ec2-获取单个
    public AwsEc2 getEc2(int id) {
        return new AwsEc2DaoImpl().getEc2(id);
    }

    // ec2-查询所有
    public List<AwsEc2> getEc2List(HashMap<String, Object> params) throws Exception {
        List<AwsEc2> list = new AwsEc2DaoImpl().getList(params);
        for (AwsEc2 ae : list) {
            ae.setUserName(this.getUserName(ae.getAccessKeyId()));
        }
        return list;
    }

    // ec2-查询所有实例的详细信息&分页
    public JSONObject getEc2List(HashMap<String, Object> params, int pageNumber, int pageSize) throws Exception {
        List<AwsEc2> list = new AwsEc2DaoImpl().getList(params, pageNumber, pageSize);
        for (AwsEc2 ae : list) {
            ae.setUserName(this.getUserName(ae.getAccessKeyId()));
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", new AwsEc2DaoImpl().getTotal(params));
        jsonObject.put("items", list);
        return jsonObject;
    }

}
