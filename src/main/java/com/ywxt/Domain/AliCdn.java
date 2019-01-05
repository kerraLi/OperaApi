package com.ywxt.Domain;


import com.aliyuncs.cdn.model.v20141111.DescribeUserDomainsResponse;

import java.lang.reflect.Field;

public class AliCdn extends DescribeUserDomainsResponse.PageData {

    private String accessKeyId;

    public AliCdn(DescribeUserDomainsResponse.PageData pageData) throws Exception {
        Field[] fields = pageData.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            Object value = fields[i].get(pageData);
            if (null != value) {
                fields[i].set(this, value);
            }
            fields[i].setAccessible(false);
        }
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }
}
