package com.ywxt.Domain.in;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class AliScdnIn {
    private List<Long> ids;
    private String abandon;

    //刷新参数
    private String operateType;
    private String refreshType;
    private String content;

    //
    private String objectType;//类型
    private String url;//url
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date startTime;
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date endTime;

    //
    private Integer page =1;
    private Integer limit = 20;
}
