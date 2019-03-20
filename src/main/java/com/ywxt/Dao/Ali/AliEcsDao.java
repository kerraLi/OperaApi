package com.ywxt.Dao.Ali;

import com.ywxt.Domain.Ali.AliEcs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.HashMap;
import java.util.List;

public interface AliEcsDao extends JpaRepository<AliEcs, Integer> {

    AliEcs getByInstanceId(String instanceId);

    void deleteByAccessKeyId(String keyId);

    List<AliEcs> findAll(Specification<AliEcs> specification);

    Page<AliEcs> findAll(Specification<AliEcs> specification, Pageable pageable);

}
