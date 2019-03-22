package com.ywxt.Dao.System;

import com.ywxt.Domain.System.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParameterDao extends JpaRepository<Parameter, Integer> {

    Parameter findParameterById(Integer id);

    Parameter findParameterByKey(String key);
}
