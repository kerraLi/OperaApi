package com.ywxt.Dao.Godaddy;

import com.ywxt.Domain.Godaddy.GodaddyAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GodaddyAccountDao extends JpaRepository<GodaddyAccount, Integer> {

    GodaddyAccount getByAccessKeyId(String keyId);
}
