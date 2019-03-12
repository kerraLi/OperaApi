package com.ywxt.Dao.ServerManage;

import com.ywxt.Domain.ServerManage.ConfigManage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConfigManageRepository extends JpaRepository<ConfigManage,Long> {

    List<ConfigManage> findByServerIdOrderByCreateTimeDesc(Long serverId);
}
