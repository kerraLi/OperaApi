package com.ywxt.Domain.Monitor.Time;

import lombok.Data;


@Data
public class MonitorPoint {

    private String name;
    private String host;
    private Integer port;
    // 已推送数量
    private Integer publish;
    // 当前状态
    private String state;
}
