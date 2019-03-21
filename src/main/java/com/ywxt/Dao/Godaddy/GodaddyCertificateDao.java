package com.ywxt.Dao.Godaddy;

import com.ywxt.Domain.Godaddy.GodaddyCertificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.HashMap;
import java.util.List;

public interface GodaddyCertificateDao extends JpaRepository<GodaddyCertificate, Integer> {

    void deleteByAccessKeyId(String keyId);

    List<GodaddyCertificate> findAll(Specification<GodaddyCertificate> specification);

    Page<GodaddyCertificate> findAll(Specification<GodaddyCertificate> specification, Pageable pageable);
}
