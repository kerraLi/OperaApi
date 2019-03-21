package com.ywxt.Service.Aws;

import com.ywxt.Domain.Aws.AwsEc2;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface AwsEc2Service {
    Page<AwsEc2> getList(Map<String, String> params);
}
