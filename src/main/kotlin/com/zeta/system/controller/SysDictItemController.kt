package com.zeta.system.controller

import cn.hutool.core.lang.Assert
import com.zeta.system.model.dto.sysDictItem.SysDictItemDTO
import com.zeta.system.model.dto.sysDictItem.SysDictItemSaveDTO
import com.zeta.system.model.dto.sysDictItem.SysDictItemUpdateDTO
import com.zeta.system.model.entity.SysDictItem
import com.zeta.system.model.param.SysDictItemQueryParam
import com.zeta.system.service.ISysDictItemService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.zetaframework.base.controller.SuperAllViewController
import org.zetaframework.base.controller.crud.goView
import org.zetaframework.base.result.ApiResult
import org.zetaframework.core.exception.ArgumentException
import org.zetaframework.core.exception.BusinessException
import org.zetaframework.core.saToken.annotation.PreAuth
import org.zetaframework.core.saToken.annotation.PreCheckPermission
import javax.servlet.http.HttpServletRequest

/**
 * <p>
 * 字典项 前端控制器
 * </p>
 *
 * @author AutoGenerator
 * @date 2022-04-15 10:12:10
 */
@Api(tags = ["字典项"])
@PreAuth(replace = "sys:dictItem")
@Controller
@RequestMapping("/system/dictItem")
class SysDictItemController: SuperAllViewController<ISysDictItemService, Long, SysDictItem, SysDictItemQueryParam, SysDictItemSaveDTO, SysDictItemUpdateDTO>() {

    /**
     * 当前模块路径
     */
    override fun getModulePath(): String {
        // 字典项与字典在同一个页面
        return "/system/dict"
    }

    /**
     * 新增页面
     *
     * @param model [Model]
     * @param entity 实体类 路由后面携带的参数会被封装到实体类里面。当然得是实体类中的字段才行
     * @param request 请求对象
     */
    override fun saveView(model: Model, entity: SysDictItem?, request: HttpServletRequest): String {
        // 获取字典id
        val dictId = entity?.dictId ?: throw ArgumentException("字典id不能为空")
        model.addAttribute("dictId", dictId)

        return goView("saveData.html")
    }

    /**
     * 修改页面
     *
     * @param model [Model]
     * @param entity 实体类 路由后面携带的参数会被封装到实体类里面。当然得是实体类中的字段才行
     * @param request 请求对象
     */
    override fun updateView(model: Model, entity: SysDictItem?, request: HttpServletRequest): String {
        // 获取字典项数据
        val dictItem = entity?.id?.let {
            service.getById(it)
        } ?: throw BusinessException("字典项不存在")
        model.addAttribute("dictItem", dictItem)

        return goView("editData.html")
    }


    /**
     * 根据字典编码查询字典项
     *
     * @param codes List<String>
     */
    @PreCheckPermission(value = ["{}:view"])
    @ApiOperation("根据字典编码查询字典项")
    @PostMapping("/codeList")
    fun codeList(@RequestBody @ApiParam("字典code") codes: List<String>): ApiResult<Map<String, List<SysDictItemDTO>>> {
        Assert.notEmpty(codes, "字典code不能为空")
        return success(service.listByCodes(codes))
    }

}
