package com.ywxt.Domain.Ali;

import com.aliyuncs.scdn.model.v20171115.DescribeScdnRefreshTasksResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Data
@Entity
@Table(name = "ali_scdn_task")
public class AliScdnTask implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String userName;
    private String accessKeyId;
    private String taskId;//任务id
    private String objectPath;//刷新对象路径
    private String status;//状态，值分别是Complete(完成)、Refreshing(刷新中)、Failed(刷新失败)、Pending(等待刷新)
    private String process;//进度百分比
    private String objectType;//任务类型(file,path,preload)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date creationTime;//任务对象创建时间，UTC时间
    private String description;//刷新预热失败返回错误描述，目前包含三种错误”Internal Error” “Origin Timeout” “Origin Return StatusCode 5XX”

    public AliScdnTask(){};

    public AliScdnTask(AliAccount account, DescribeScdnRefreshTasksResponse.Task task) throws ParseException {
        this.userName = account.getUserName();
        this.accessKeyId = account.getAccessKeyId();
        this.taskId = task.getTaskId();
        this.objectPath = task.getObjectPath();
        this.status = task.getStatus();
        this.process = task.getProcess();
        this.objectType = task.getObjectType();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss Z");
        df.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        this.creationTime = df.parse(task.getCreationTime().replace("Z", " UTC"));
        this.description = task.getDescription();
    }
}
