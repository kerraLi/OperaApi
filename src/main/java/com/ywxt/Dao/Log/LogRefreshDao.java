package com.ywxt.Dao.Log;

import com.ywxt.Domain.Log.LogRefresh;

public interface LogRefreshDao {

    public LogRefresh getLast(String type);

    public int saveLogRefresh(LogRefresh logRefresh);
}
