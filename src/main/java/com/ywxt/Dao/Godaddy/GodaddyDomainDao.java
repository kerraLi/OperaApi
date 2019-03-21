package com.ywxt.Dao.Godaddy;

import com.ywxt.Domain.Godaddy.GodaddyDomain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.HashMap;
import java.util.List;

public interface GodaddyDomainDao extends JpaRepository<GodaddyDomain, Integer> {
    void deleteByAccessKeyId(String keyId);

    List<GodaddyDomain> findAll(Specification<GodaddyDomain> specification);

    Page<GodaddyDomain> findAll(Specification<GodaddyDomain> specification, Pageable pageable);

}

