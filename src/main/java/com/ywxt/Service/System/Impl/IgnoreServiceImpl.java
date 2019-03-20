package com.ywxt.Service.System.Impl;

import com.ywxt.Dao.System.IgnoreDao;
import com.ywxt.Domain.System.Ignore;
import com.ywxt.Service.System.IgnoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class IgnoreServiceImpl implements IgnoreService {

    @Autowired
    private IgnoreDao ignoreDao;

    @Transactional
    public void setMarked(String status, Ignore ignore) {
        if (status.equals("mark")) {
            ignoreDao.saveAndFlush(ignore);
        } else {
            ignoreDao.deleteAllByDomainAndMarkKeyAndMarkValue(ignore.getDomain(), ignore.getMarkKey(), ignore.getMarkValue());
        }
    }

    @Transactional
    public void setMarked(String status, List<Ignore> list) {
        for (Ignore ignore : list) {
            if (status.equals("mark")) {
                ignoreDao.save(ignore);
            } else {
                ignoreDao.deleteAllByDomainAndMarkKeyAndMarkValue(ignore.getDomain(), ignore.getMarkKey(), ignore.getMarkValue());
            }
        }
        ignoreDao.flush();
    }

    // 批量获取标记数据
    public String[] getMarkedValues(String domain) {
        List<Ignore> list = ignoreDao.findByDomain(domain);
        String[] strings = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            strings[i] = list.get(i).getMarkValue();
        }
        return strings;
    }

}
