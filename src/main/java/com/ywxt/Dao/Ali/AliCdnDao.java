package com.ywxt.Dao.Ali;

import com.ywxt.Domain.Ali.AliCdn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.HashMap;
import java.util.List;

public interface AliCdnDao extends JpaRepository<AliCdn, Integer> {

    void deleteByAccessKeyId(String keyId);

    AliCdn getByDomainName(String domainName);

}
