package com.ywxt.Domain;

import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;

import java.lang.reflect.Field;

public class AliEcs extends DescribeInstancesResponse.Instance {
    private String accessKeyId;
    private Boolean isAlertExpired = false;

    public AliEcs(DescribeInstancesResponse.Instance instance) throws Exception {
        Field[] fields = instance.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            Object value = fields[i].get(instance);
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

    public Boolean getAlertExpired() {
        return isAlertExpired;
    }

    public void setAlertExpired(Boolean alertExpired) {
        isAlertExpired = alertExpired;
    }
}
