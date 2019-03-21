package com.ywxt.Dao.Aws;

import com.ywxt.Domain.Aws.AwsAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AwsAccountDao extends JpaRepository<AwsAccount, Integer> {
    AwsAccount getByAccessKeyId(String keyId);
}
