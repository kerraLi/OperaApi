package com.ywxt.Controller.ServerManage;

import com.ywxt.Controller.CommonController;
import com.ywxt.Domain.ApiResult;
import com.ywxt.Domain.ServerManage.ServerInfo;
import com.ywxt.Service.ServerManage.ServerInfoService;
import com.ywxt.Utils.ExceptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/serverManage")
public class ServerInfoController extends CommonController{

    @Autowired
    private ServerInfoService serverInfoService;

    /**
     * 添加或者更新服务器信息
     * @param serverInfo
     * @return
     */
    @PostMapping("/addOrUpdate")
    public ApiResult addOrUpdate(@RequestBody ServerInfo serverInfo){
        ExceptionUtil.isTrue(serverInfo.getIp()==null,"服务器ip地址不能为空");
        ExceptionUtil.isTrue(serverInfo.getName()==null,"服务器名称不能为空");
        ExceptionUtil.isTrue(serverInfo.getOperator()==null,"服务器运营商不能为空");
        if(serverInfo.getId()==null){
            serverInfoService.add(serverInfo);
        }else{
            ExceptionUtil.isTrue(serverInfo.getId()<1,"服务器id不合法");
            serverInfoService.update(serverInfo);
        }
        return ApiResult.success();
    }

    @PostMapping("/getById/{id}")
    public ApiResult getById(@PathVariable Long id){
        ExceptionUtil.isTrue(id==null||id<1,"服务器id不合法");
        return ApiResult.successWithObject(serverInfoService.getById(id));
    }

    /**
     * 分页查询
     * @param serverInfo
     * @param page
     * @param limit
     * @return
     */
    @PostMapping("/list/{page}/{limit}")
    public ApiResult list(@RequestBody ServerInfo serverInfo,@PathVariable() Integer page,@PathVariable Integer limit){
        page = page==null ? 1:page;
        limit = limit==null ? 10:limit;
        return ApiResult.successWithObject(serverInfoService.list(serverInfo,page,limit));
    }

    @PostMapping("/delete/{id}")
    public ApiResult list(@PathVariable Long id){
        ExceptionUtil.isTrue(id==null||id<1,"服务器id不合法");
        serverInfoService.delete(id);
        return ApiResult.success();
    }
}
