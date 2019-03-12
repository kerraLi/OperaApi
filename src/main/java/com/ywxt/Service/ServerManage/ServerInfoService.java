package com.ywxt.Service.ServerManage;

import com.ywxt.Dao.ServerManage.ServerInfoRepository;
import com.ywxt.Domain.ServerManage.ServerInfo;
import com.ywxt.Utils.ExceptionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ServerInfoService {
    @Autowired
    private ServerInfoRepository serverInfoRepository;


    @Transactional
    public void add(ServerInfo serverInfo) {
        serverInfo.setCreateTime(new Date());
        serverInfo.setUpdateTime(new Date());
        serverInfoRepository.saveAndFlush(serverInfo);
    }

    @Transactional
    public void update(ServerInfo serverInfo) {
        ServerInfo oldServerInfo = getById(serverInfo.getId());
        ExceptionUtil.isTrue(oldServerInfo==null,"服务器不存在");
        serverInfo.setCreateTime(oldServerInfo.getCreateTime());
        serverInfo.setUpdateTime(new Date());
        serverInfoRepository.saveAndFlush(serverInfo);
    }

    public ServerInfo getById(Long id) {
        Optional<ServerInfo> byId = serverInfoRepository.findById(id);
        ExceptionUtil.isTrue(!byId.isPresent(),"服务器id错误");
        return byId.get();
    }

    /**
     * 分页查询
     * @param serverInfo
     * @param page
     * @param limit
     * @return
     */
    public Page<ServerInfo> list(ServerInfo serverInfo, int page, int limit) {

        Specification<ServerInfo> specification = new Specification<ServerInfo>() {
            @Override
            public Predicate toPredicate(Root<ServerInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if(StringUtils.isNoneBlank(serverInfo.getName())){
                    Predicate likeName = criteriaBuilder.like(root.get("name").as(String.class),"%"+serverInfo.getName()+"%");
                    predicates.add(likeName);
                }
                if(StringUtils.isNoneBlank(serverInfo.getOperator())){
                    Predicate operator = criteriaBuilder.equal(root.get("operator").as(String.class),serverInfo.getOperator());
                    predicates.add(operator);
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        Pageable pageable = PageRequest.of(page-1,limit);
        return serverInfoRepository.findAll(specification,pageable);
    }

    public void delete(Long id) {
        serverInfoRepository.deleteById(id);
    }

    public ServerInfo getByIp(String serverIp) {
        return serverInfoRepository.getByIp(serverIp);
    }
}
