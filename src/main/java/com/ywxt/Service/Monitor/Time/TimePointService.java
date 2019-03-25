package com.ywxt.Service.Monitor.Time;

import com.ywxt.Domain.Monitor.Time.MonitorPoint;

import java.util.List;

public interface TimePointService {

    List<MonitorPoint> getList() throws Exception;

}
