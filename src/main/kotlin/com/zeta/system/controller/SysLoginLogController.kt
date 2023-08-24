package com.zeta.system.controller

import cn.hutool.core.util.StrUtil
import com.zeta.system.model.entity.SysLoginLog
import com.zeta.system.model.param.SysLoginLogQueryParam
import com.zeta.system.service.ISysLoginLogService
import io.swagger.annotations.Api
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.zetaframework.base.controller.SuperSimpleController
import org.zetaframework.base.controller.curd.QueryController
import org.zetaframework.base.controller.view.IndexView
import org.zetaframework.base.param.PageParam
import org.zetaframework.base.result.PageResult
import org.zetaframework.core.log.annotation.SysLog
import org.zetaframework.core.saToken.annotation.PreAuth

/**
 * 登录日志 前端控制器
 *
 * @author AutoGenerator
 * @date 2022-03-21 16:33:13
 */
@Api(tags = ["登录日志"])
@PreAuth(replace = "sys:loginLog")
@Controller
@RequestMapping("/system/loginLog")
class SysLoginLogController : SuperSimpleController<ISysLoginLogService, SysLoginLog>(),
    QueryController<SysLoginLog, Long, SysLoginLogQueryParam>,
    IndexView<SysLoginLog>
{
    /**
     * 当前模块路径
     */
    override fun getModulePath(): String {
        return "/system/loginLog"
    }


    /**
     * 分页查询
     *
     * @param pageParam PageParam 分页参数：page、limit、order、sort等
     * @param queryParam QueryParam 查询条件
     * @return PageResult<Entity>
     */
    @SysLog(enabled = false)
    override fun page(pageParam: PageParam, queryParam: SysLoginLogQueryParam?): PageResult<SysLoginLog> {
        // 默认排序
        if (StrUtil.isBlank(pageParam.sort)) {
            pageParam.setDefaultDesc("id")
        }
        return service.customPage(pageParam, queryParam)
    }
}
