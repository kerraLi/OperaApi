package com.ywxt.Dao.System;

import com.ywxt.Domain.System.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParameterDao extends JpaRepository<Parameter, Integer> {

    Parameter getByKey(String key);
}
