package com.ywxt.Controller.ServerManage;

import com.ywxt.Annotation.NotOperationAction;
import com.ywxt.Annotation.PassToken;
import com.ywxt.Domain.ApiResult;
import com.ywxt.Domain.ServerManage.ConfigManage;
import com.ywxt.Service.ServerManage.ConfigManageService;
import com.ywxt.Utils.ExceptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/configManage")
public class ConfigManageController {

    @Autowired
    private ConfigManageService configManageService;

    /**
     * 上传文件
     * @param file
     * @param id
     * @param fileType
     * @return
     * @throws IOException
     */
    @RequestMapping("/upload")
    public ApiResult upload(@RequestParam(value = "file", required = false) MultipartFile file,
                            @RequestParam(value = "id", required = false) Long id,
                            @RequestParam(value = "fileType", required = false) String fileType) throws IOException {
        ExceptionUtil.isTrue(file==null,"没有接受到文件");
        ExceptionUtil.isTrue(id==null,"服务器id不能为空");
        ExceptionUtil.isTrue(fileType==null,"文件类型不能为空");
        configManageService.upload(file,id,fileType);
        return ApiResult.success();
    }

    /**
     * 更新状态
     * @param configManage
     * @return
     */
    @PassToken(login =true )
    @NotOperationAction
    @RequestMapping("/updateState")
    public ApiResult updateState(@RequestBody ConfigManage configManage){
        ExceptionUtil.isTrue(configManage.getRunResult()==null,"配置文件的运行结果不能为空");
         ExceptionUtil.isTrue(configManage.getId()==null,"id不能为空");
        configManageService.updateState(configManage.getRunResult(),configManage.getId());
        return ApiResult.success();
    }

    /**
     * 根据服务器id获取文件上传记录
     * @param configManage
     * @return
     */
    @RequestMapping("/list")
    public ApiResult list(@RequestBody ConfigManage configManage) {
        ExceptionUtil.isTrue(configManage.getServerId() == null, "服务器id不能为空");
        return ApiResult.successWithObject(configManageService.list(configManage.getServerId()));
    }
}
