package com.ywxt.Dao.ServerManage;

import com.ywxt.Domain.ServerManage.ServerInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServerInfoRepository extends JpaRepository<ServerInfo,Long> {
    Page<ServerInfo> findAll(Specification<ServerInfo> specification, Pageable pageable);

    ServerInfo getByIp(String serverIp);
}
