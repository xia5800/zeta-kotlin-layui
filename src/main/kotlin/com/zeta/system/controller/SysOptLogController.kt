package com.zeta.system.controller

import cn.hutool.core.util.StrUtil
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport
import com.zeta.system.model.dto.sysOptLog.SysOptLogTableDTO
import com.zeta.system.model.entity.SysOptLog
import com.zeta.system.model.param.SysOptLogQueryParam
import com.zeta.system.service.ISysOptLogService
import com.zeta.system.service.ISysUserService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.zetaframework.base.controller.SuperSimpleController
import org.zetaframework.base.controller.goView
import org.zetaframework.base.controller.view.IndexView
import org.zetaframework.base.param.PageParam
import org.zetaframework.base.result.PageResult
import org.zetaframework.core.exception.BusinessException
import org.zetaframework.core.saToken.annotation.PreAuth
import org.zetaframework.core.saToken.annotation.PreCheckPermission
import springfox.documentation.annotations.ApiIgnore
import javax.servlet.http.HttpServletRequest

/**
 * 操作日志 前端控制器
 *
 * @author gcc
 * @date 2022-03-18 15:27:15
 */
@Api(tags = ["操作日志"])
@PreAuth(replace = "sys:optLog")
@Controller
@RequestMapping("/system/optLog")
class SysOptLogController(
    private val userService: ISysUserService
) : SuperSimpleController<ISysOptLogService, SysOptLog>(),
    IndexView<SysOptLog>
{
    /**
     * 当前模块路径
     */
    override fun getModulePath(): String {
        return "/system/optLog"
    }

    /**
     * 详情页
     *
     * @param model [Model]
     * @param entity 实体类 路由后面携带的参数会被封装到实体类里面。当然得是实体类中的字段才行
     * @param request 请求对象
     */
    @ApiIgnore
    @PreCheckPermission(value = ["{}:view"])
    @GetMapping("/detail")
    fun view(model: Model, entity: SysOptLog?, request: HttpServletRequest): String {
        // 查询操作日志
        val optLog = entity?.id?.let {
            service.getById(it)
        } ?: throw BusinessException("日志不存在")

        // 查询操作人
        val user = userService.getById(optLog.createdBy)
        optLog.userName = user?.username
        model.addAttribute("optLog", optLog)

        return goView("detail.html")
    }


    /**
     * 分页查询
     *
     * @param pageParam PageParam 分页参数：page、limit、order、sort等
     * @param queryParam QueryParam 查询条件
     * @return PageResult<Entity>
     */
    @PreCheckPermission(value = ["{}:view"])
    @ApiOperationSupport(order = 10)
    @ApiOperation(value = "分页查询")
    @ResponseBody
    @GetMapping("/page")
    fun page(pageParam: PageParam, queryParam: SysOptLogQueryParam?): PageResult<SysOptLogTableDTO> {
        // 默认排序
        if (StrUtil.isBlank(pageParam.sort)) {
            pageParam.setDefaultDesc("id")
        }
        return service.pageTable(pageParam, queryParam)
    }


}
