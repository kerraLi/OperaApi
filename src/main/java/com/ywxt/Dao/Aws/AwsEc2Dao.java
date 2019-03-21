package com.ywxt.Dao.Aws;

import com.ywxt.Domain.Aws.AwsEc2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AwsEc2Dao extends JpaRepository<AwsEc2, Integer> {

    void deleteByAccessKeyId(String keyId);

    Page<AwsEc2> findAll(Specification<AwsEc2> specification, Pageable pageable);

}
