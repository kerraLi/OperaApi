package com.ywxt.Controller.Ali;

import com.ywxt.Domain.Ali.AliScdn;
import com.ywxt.Domain.ApiResult;
import com.ywxt.Domain.in.AliScdnIn;
import com.ywxt.Service.Ali.Impl.AliScdnService;
import com.ywxt.Utils.DateUtil;
import com.ywxt.Utils.ExceptionUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("ali/scdn")
public class AliScdnController {

    @Autowired
    private AliScdnService aliScdnService;

    /**
     * 获取scdn列表
     *
     * @param aliScdn
     * @param page
     * @param limit
     * @return
     */
    @PostMapping("/list/{page}/{limit}")
    public ApiResult getList(@RequestBody AliScdn aliScdn, @PathVariable() Integer page, @PathVariable Integer limit) {
        page = page == null ? 1 : page;
        limit = limit == null ? 10 : limit;
        return ApiResult.successWithObject(aliScdnService.getList(aliScdn, page, limit));
    }

    /**
     * 修改弃用
     *
     * @param aliScdnIn
     * @return
     */
    @PostMapping("/updateAbandon")
    public ApiResult updateAbandon(@RequestBody AliScdnIn aliScdnIn) {
        ExceptionUtil.isTrue(CollectionUtils.isEmpty(aliScdnIn.getIds()), "没有需要修改的数据");
        ExceptionUtil.isTrue(!("1".equals(aliScdnIn.getAbandon()) || "0".equals(aliScdnIn.getAbandon())), "参数不正确");
        aliScdnService.updateAbandon(aliScdnIn);
        return ApiResult.success();
    }

    /**
     * 添加刷新和预热
     */
    @PostMapping("/addRefresh")
    public ApiResult addRefresh(@RequestBody AliScdnIn aliScdnIn) throws Exception {
        ExceptionUtil.isTrue(StringUtils.isEmpty(aliScdnIn.getOperateType()), "操作类型不能为空");
        ExceptionUtil.isTrue(StringUtils.isEmpty(aliScdnIn.getRefreshType()), "刷新类型不能为空");
        ExceptionUtil.isTrue(StringUtils.isEmpty(aliScdnIn.getContent()), "路由不能为空");
        aliScdnService.addRefresh(aliScdnIn);
        return ApiResult.success();
    }

    /**
     * 获取刷新（预热）列表
     *
     * @param aliScdnIn
     * @return
     */
    @PostMapping("/refreshList")
    public ApiResult refreshList(@RequestBody AliScdnIn aliScdnIn) {
        if (aliScdnIn.getEndTime() != null) {
            aliScdnIn.setEndTime(DateUtil.setEndDate(aliScdnIn.getEndTime()));
        }
        return ApiResult.successWithObject(aliScdnService.refreshList(aliScdnIn));
    }

    /**
     * 更新刷新（预热）的状态 单条
     *
     * @param id
     * @return
     */
    @PostMapping("/updateScdn/{id}")
    public ApiResult updateScdn(@PathVariable Long id) throws Exception {
        ExceptionUtil.isTrue(id == null || id < 0, "id错误");
        aliScdnService.updateScdn(id);
        return ApiResult.success();
    }
}
