package com.ywxt.Dao.System;

import com.ywxt.Domain.System.Ignore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IgnoreDao extends JpaRepository<Ignore, Integer> {

    List<Ignore> findByDomain(String string);

    void deleteAllByDomainAndMarkKeyAndMarkValue(String domain, String markKey, String markValue);
}
