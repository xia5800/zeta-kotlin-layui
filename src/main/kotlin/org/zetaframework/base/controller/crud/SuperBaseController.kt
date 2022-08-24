package org.zetaframework.base.controller.crud

import cn.hutool.core.text.StrPool
import cn.hutool.core.util.StrUtil
import org.zetaframework.base.result.ApiResult

/**
 * 基础接口
 *
 * @param <Entity> 实体
 * @author gcc
 */
interface SuperBaseController {

    /**
     * 当前模块路径
     *
     * 说明：
     * 1.新增、修改、列表页面会拼接这个路径
     * 2.由具体的业务Controller实现
     *
     * 例如：
     * modulePath: system/user
     * indexPath: system/user/index.html
     * updatePath: system/user/edit.html
     * addPath: system/user/add.html
     */
    fun getModulePath(): String

    /**
     * 重定向
     *
     * @param url
     */
    fun redirect(url: String): String {
        return "redirect:${url}"
    }

    /**
     * 返回成功
     *
     * @return ApiResult<T>
     */
    fun <T> success(): ApiResult<T> = ApiResult.success()

    /**
     * 返回成功
     *
     * @param message String
     * @return ApiResult<T>
     */
    fun <T> success(message: String): ApiResult<T> = ApiResult.success(message = message)

    /**
     * 返回成功
     *
     * @param data T
     * @return ApiResult<T>
     */
    fun <T> success(data: T): ApiResult<T> = ApiResult.success(data = data)

    /**
     * 返回成功
     *
     * @param message String
     * @param data T
     * @return ApiResult<T>
     */
    fun <T> success(message: String, data: T): ApiResult<T> = ApiResult.success(message = message, data = data)

    /**
     * 返回失败
     *
     * @return ApiResult<T>
     */
    fun <T> fail(): ApiResult<T> = ApiResult.fail()

    /**
     * 返回失败
     *
     * @param message String
     * @return ApiResult<T>
     */
    fun <T> fail(message: String): ApiResult<T> = ApiResult.fail(message = message)

    /**
     * 返回失败
     *
     * @param data T
     * @return ApiResult<T>
     */
    fun <T> fail(data: T): ApiResult<T> = ApiResult.fail(data = data)

    /**
     * 返回失败
     *
     * @param message String
     * @param data T
     * @return ApiResult<T>
     */
    fun <T> fail(message: String, data: T): ApiResult<T> = ApiResult.fail(message = message, data = data)

}

/**
 * 扩展方法：当前模块路径去掉头尾的"/"
 */
fun SuperBaseController.getTrimModulePath(): String {
    return getModulePath().trim(StrPool.C_SLASH)
}

/**
 * 扩展方法：前往指定页面
 *
 * 说明：
 * 前往当前模块下的页面
 *
 * @param viewName 视图名字
 */
fun SuperBaseController.goView(viewName: String): String {
    return goView(getTrimModulePath(), viewName)
}

/**
 * 扩展方法：前往指定页面
 *
 * 说明：
 * 前往指定模块下的页面
 *
 * @param modulePath 视图所在模块路径
 * @param viewName 视图名字
 */
fun SuperBaseController.goView(modulePath: String, viewName: String): String {
    return StrUtil.join(StrPool.SLASH, modulePath, viewName.trim(StrPool.C_SLASH))
}
