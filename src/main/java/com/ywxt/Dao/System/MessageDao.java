package com.ywxt.Dao.System;

import com.ywxt.Domain.System.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageDao extends JpaRepository<Message, Integer> {

    int countByStatus(String status);

    Page<Message> findAll(Specification<Message> specification, Pageable pageable);

}
