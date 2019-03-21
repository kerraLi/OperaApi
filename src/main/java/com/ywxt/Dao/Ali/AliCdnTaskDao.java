package com.ywxt.Dao.Ali;

import com.ywxt.Domain.Ali.AliCdnTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AliCdnTaskDao extends JpaRepository<AliCdnTask, Integer> {

    Page<AliCdnTask> findAll(Specification<AliCdnTask> specification, Pageable pageable);

}
