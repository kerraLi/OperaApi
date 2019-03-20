package com.ywxt.Domain.Ali;

import com.aliyuncs.cdn.model.v20141111.DescribeRefreshTasksResponse;
import com.ywxt.Annotation.NotFilterColumn;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Entity
@Table(name = "ali_cdn_task")
public class AliCdnTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotFilterColumn
    @Transient
    private String userName;
    private String accessKeyId;
    private String taskId;
    private Date creationTime;
    // path || file || preload
    private String objectType;
    private String objectPath;
    private String process;
    private String description;
    // Complete || Refreshing || Failed || Pending
    private String status;

    public AliCdnTask() {
    }

    public AliCdnTask(String accessKeyId, DescribeRefreshTasksResponse.CDNTask cdnTask) throws Exception {
        this.accessKeyId = accessKeyId;
        this.taskId = cdnTask.getTaskId();
        this.objectType = cdnTask.getObjectType();
        this.objectPath = cdnTask.getObjectPath();
        this.process = cdnTask.getProcess();
        this.description = cdnTask.getDescription();
        this.status = cdnTask.getStatus();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss Z");
        df.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        this.creationTime = df.parse(cdnTask.getCreationTime().replace("Z", " UTC"));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }


    public String getObjectPath() {
        return objectPath;
    }

    public void setObjectPath(String objectPath) {
        this.objectPath = objectPath;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
