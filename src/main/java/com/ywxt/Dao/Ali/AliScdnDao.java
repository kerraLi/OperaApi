package com.ywxt.Dao.Ali;

import com.ywxt.Domain.Ali.AliScdn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AliScdnDao extends JpaRepository<AliScdn, Long> {

    void deleteByAccessKeyId(String keyId);

    List<AliScdn> findByAccessKeyId(String accessKeyId);

    Page<AliScdn> findAll(Specification<AliScdn> specification, Pageable pageable);

    AliScdn findByDomainName(String objectPath);
}
