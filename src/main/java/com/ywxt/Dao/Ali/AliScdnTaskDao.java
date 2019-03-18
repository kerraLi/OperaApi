package com.ywxt.Dao.Ali;

import com.ywxt.Domain.Ali.AliScdnTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AliScdnTaskDao extends JpaRepository<AliScdnTask,Long> {

    Page<AliScdnTask> findAll(Specification<AliScdnTask> specification, Pageable pageable);
}
