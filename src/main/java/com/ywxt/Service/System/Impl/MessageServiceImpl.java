package com.ywxt.Service.System.Impl;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Annotation.NotFilterColumn;
import com.ywxt.Command.Websocket;
import com.ywxt.Dao.System.MessageDao;
import com.ywxt.Domain.System.Message;
import com.ywxt.Service.System.MessageService;
import com.ywxt.Service.System.ParameterService;
import com.ywxt.Utils.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.util.*;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageDao messageDao;
    @Autowired
    private ParameterService parameterService;

    // 获取类型
    public List<String> getTypes() {
        Map<String, String> map = Parameter.MessageTitles;
        List<String> list = new ArrayList<>();
        for (String s : map.values()) {
            if (!list.contains(s)) {
                list.add(s);
            }
        }
        return list;
    }

    // 获取数量
    public int getCount(String status) {
        return messageDao.countByStatus(status);
    }

    // 查询所有&分页
    public Page<Message> getList(Map<String, String> params) throws Exception {
        int pageNumber = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 1;
        int pageSize = params.containsKey("limit") ? Integer.parseInt(params.get("limit")) : 10;
        // 处理查询条件
        Specification<Message> specification = new Specification<Message>() {
            @Override
            public Predicate toPredicate(Root<Message> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                // filter批量过滤
                if (params.containsKey("key")) {
                    String filter = "%" + params.get("key") + "%";
                    Field[] fields = Message.class.getDeclaredFields();
                    // 多个or条件
                    List<Predicate> psOr = new ArrayList<>();
                    for (Field f : fields) {
                        if (f.getType() == String.class && !f.isAnnotationPresent(NotFilterColumn.class)) {
                            psOr.add(cb.like(root.get(f.getName()).as(String.class), filter));
                        }
                    }
                    predicates.add(cb.or(psOr.toArray(new Predicate[psOr.size()])));
                }
                if (params.containsKey("status")) {
                    Predicate predicate = cb.equal(root.get("status").as(String.class), params.get("status"));
                    predicates.add(predicate);
                }
                if (params.containsKey("title")) {
                    Predicate predicate = cb.equal(root.get("title").as(String.class), params.get("title"));
                    predicates.add(predicate);
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        Sort sort = new Sort(Sort.Direction.DESC, "createdTime");
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
        return messageDao.findAll(specification, pageable);
    }

    // 批量设置状态
    public void setAllStatus(List<Integer> ids, String status) {
        for (Integer id : ids) {
            Message message = messageDao.findMessageById(id);
            message.setStatus(status);
            message.setModifyTime(new Date());
            messageDao.save(message);
        }
        messageDao.flush();
    }

    // 设置状态
    public void setStatus(int id, String status) {
        Message message = messageDao.findMessageById(id);
        message.setStatus(status);
        message.setModifyTime(new Date());
        messageDao.saveAndFlush(message);
    }

    // 新建消息
    public void create(String action, String themeId, Map<String, String> msgParam) throws Exception {
        this.create(action, themeId, msgParam, new HashMap<>());
    }

    // 新建消息
    public void create(String action, String themeId, Map<String, String> msgParam, Map<String, String> otherParam) throws Exception {
        Message message = new Message();
        String context = this.getContextByAction(action);
        for (Map.Entry<String, String> e : msgParam.entrySet()) {
            String value = (e.getValue() == null) ? "" : e.getValue();
            context = context.replace("{" + e.getKey() + "}", value);
        }
        // 存数据转换城br
        context = context.replace("\r\n", "</br>");
        String[] actions = action.split("_");
        // 额外设置参数 目前“webhook”参数
        if (otherParam.size() > 0) {
            message.setImageUrl(otherParam.get("imageUrl"));
            message.setEvalMatches(otherParam.get("evalMatches"));
            context = context + "</br><img src='" + otherParam.get("imageUrl") + "'>";
        }
        message.setTitle(Parameter.MessageTitles.get(action));
        message.setTheme(actions[0]);
        message.setThemeId(themeId);
        message.setMessage(context);
        message.setCreatedTime(new Date());
        message.setModifyTime(new Date());
        message.setStatus("new");
        messageDao.saveAndFlush(message);
        // 发送ws消息
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("timestamp", System.currentTimeMillis());
        jsonObject.put("title", message.getTitle());
        jsonObject.put("action", "message");
        jsonObject.put("status", message.getStatus());
        jsonObject.put("id", message.getId());
        jsonObject.put("themeId", message.getThemeId());
        jsonObject.put("message", message.getMessage());
        if (message.hasImageUrl()) {
            jsonObject.put("imageUrl", message.getImageUrl());
        }
        this.sendWebsocket(jsonObject);
    }

    // 发送ws消息
    private void sendWebsocket(JSONObject jsonObject) {
        Websocket.sendMessageToAllUser(jsonObject.toJSONString());
    }

    // 根据action获取输出内容
    private String getContextByAction(String action) {
        String context = Parameter.MessageActions.get(action);
        List<com.ywxt.Domain.System.Parameter> ps = parameterService.getList();
        for (com.ywxt.Domain.System.Parameter p : ps) {
            context = context.replace("{" + p.getKey() + "}", p.getValue());
        }
        return context;
    }
}
