package com.ywxt.Service.System;

import com.ywxt.Domain.System.Message;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface MessageService {

    // 获取类型
    List<String> getTypes();

    // 获取数量
    int getCount(String status);

    // 查询所有&分页
    Page<Message> getList(Map<String, String> params) throws Exception;

    // 批量设置状态
    void setAllStatus(List<Integer> ids, String status);

    // 设置状态
    void setStatus(int id, String status);

    // 新建消息
    void create(String action, String themeId, Map<String, String> msgParam, Map<String, String> otherParam) throws Exception;
}
