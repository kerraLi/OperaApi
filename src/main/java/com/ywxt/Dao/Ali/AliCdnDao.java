package com.ywxt.Dao.Ali;

import com.ywxt.Domain.Ali.AliCdn;
import com.ywxt.Domain.Ali.AliEcs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.HashMap;
import java.util.List;

public interface AliCdnDao extends JpaRepository<AliCdn, Integer> {

    void deleteByAccessKeyId(String keyId);

    AliCdn getByDomainName(String domainName);

    List<AliCdn> findAll(Specification<AliCdn> specification);

    Page<AliCdn> findAll(Specification<AliCdn> specification, Pageable pageable);

}
