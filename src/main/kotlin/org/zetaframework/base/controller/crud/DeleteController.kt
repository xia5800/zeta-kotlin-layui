package org.zetaframework.base.controller.crud

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import org.zetaframework.base.result.ApiResult
import org.zetaframework.core.log.annotation.SysLog
import org.zetaframework.core.saToken.annotation.PreCheckPermission
import org.zetaframework.core.saToken.annotation.PreMode
import java.io.Serializable

/**
 * 基础删除 Controller
 *
 * @param <Entity> 实体
 * @param <Id>     主键字段类型
 * @author gcc
 */
interface DeleteController<Entity, Id: Serializable>: BaseController<Entity> {

    /**
     * 单体删除
     *
     * @param id
     * @return R<Boolean>
     */
    @PreCheckPermission(value = ["{}:delete", "{}:remove"], mode = PreMode.OR)
    @ApiOperationSupport(order = 60, author = "AutoGenerate")
    @ApiOperation(value = "单体删除")
    @SysLog
    @ResponseBody
    @DeleteMapping("/remove")
    fun delete(id: Id): ApiResult<Boolean> {
        val result = handlerDelete(id)
        if(result.defExec) {
            result.setData(getBaseService().removeById(id))
        }
        return result
    }

    /**
     * 自定义单体删除
     *
     * @param id Id
     * @return R<Boolean>
     */
    fun handlerDelete(id: Id): ApiResult<Boolean> {
        return ApiResult.successDef()
    }

    /**
     * 批量删除
     *
     * @param ids List<Long>
     * @return R<Boolean>
     */
    @PreCheckPermission(value = ["{}:delete", "{}:remove"], mode = PreMode.OR)
    @ApiOperationSupport(order = 70, author = "AutoGenerate")
    @ApiOperation(value = "批量删除")
    @SysLog
    @ResponseBody
    @DeleteMapping("/batchRemove")
    fun batchDelete(@RequestBody ids: MutableList<Id>): ApiResult<Boolean> {
        val result = handlerBatchDelete(ids)
        if(result.defExec) {
            result.setData(getBaseService().removeByIds(ids))
        }
        return result
    }

    /**
     * 自定义批量删除
     *
     * @param ids Id
     * @return R<Boolean>
     */
    fun handlerBatchDelete(ids: MutableList<Id>): ApiResult<Boolean> {
        return ApiResult.successDef()
    }
}
