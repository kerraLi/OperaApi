package com.ywxt.Dao.Ali;

import com.ywxt.Domain.Ali.AliAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AliAccountDao extends JpaRepository<AliAccount, Integer> {

    AliAccount findAliAccountById(Integer id);

    AliAccount findAliAccountByAccessKeyId(String keyId);
}