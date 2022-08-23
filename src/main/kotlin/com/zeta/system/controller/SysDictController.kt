package com.zeta.system.controller

import com.zeta.system.model.dto.sysDict.SysDictSaveDTO
import com.zeta.system.model.dto.sysDict.SysDictUpdateDTO
import com.zeta.system.model.entity.SysDict
import com.zeta.system.model.param.SysDictQueryParam
import com.zeta.system.service.ISysDictService
import io.swagger.annotations.Api
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.zetaframework.base.controller.SuperAllViewController
import org.zetaframework.base.controller.extra.ExistenceController
import org.zetaframework.core.exception.BusinessException
import org.zetaframework.core.saToken.annotation.PreAuth
import javax.servlet.http.HttpServletRequest

/**
 * <p>
 * 字典 前端控制器
 * </p>
 *
 * @author AutoGenerator
 * @date 2022-04-15 10:12:09
 */
@Api(tags = ["字典"])
@PreAuth(replace = "sys:dict")
@Controller
@RequestMapping("/system/dict")
class SysDictController:
    SuperAllViewController<ISysDictService, Long, SysDict, SysDictQueryParam, SysDictSaveDTO, SysDictUpdateDTO>(),
    ExistenceController<SysDict, Long>
{

    /**
     * 当前模块路径
     */
    override fun getModulePath(): String {
        return "/system/dict"
    }

    /**
     * 进入修改页面前的一些处理
     *
     * 说明：
     * 你可以在这里向修改页面传参
     *
     * @param model [Model]
     * @param entity 实体类 路由后面携带的参数会被封装到实体类里面。当然得是实体类中的字段才行
     * @param request 请求对象
     */
    override fun beforeUpdateView(model: Model, entity: SysDict?, request: HttpServletRequest) {
        // 获取字典数据
        val dict = entity?.id?.let {
            service.getById(it)
        } ?: throw BusinessException("字典不存在")

        model.addAttribute("dict", dict)
    }
}
