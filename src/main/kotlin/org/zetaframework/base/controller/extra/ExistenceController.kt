package org.zetaframework.base.controller.extra

import io.swagger.annotations.ApiOperation
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import org.zetaframework.base.controller.crud.BaseController
import org.zetaframework.base.param.ExistParam
import org.zetaframework.base.result.ApiResult
import org.zetaframework.core.saToken.annotation.PreCheckPermission

/**
 * 验证存在 Controller
 *
 * @param <Entity> 实体
 * @param <Id>     主键字段类型
 * @author gcc
 */
interface ExistenceController<Entity, Id>: BaseController<Entity> {

    /**
     * 验证字段是否存在
     * @param param ExistenceParam<Entity, Id>
     * @return ApiResult<Boolean>
     */
    @PreCheckPermission(value = ["{}:view"])
    @ApiOperation("验证字段是否存在")
    @ResponseBody
    @PostMapping("/existence")
    fun existence(@RequestBody @Validated param: ExistParam<Entity, Id>): ApiResult<Boolean> {
        if(param.isExist(getBaseService())) {
            return success("${param.value}已存在", true)
        }
        return success("${param.value}不存在", false)
    }

}
