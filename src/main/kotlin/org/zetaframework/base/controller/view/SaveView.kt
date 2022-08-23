package org.zetaframework.base.controller.view

import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.zetaframework.base.controller.crud.BaseController
import org.zetaframework.base.controller.crud.goView
import springfox.documentation.annotations.ApiIgnore
import javax.servlet.http.HttpServletRequest

/**
 * 新增页视图
 *
 * 说明:
 * 跳转到`modules/save.html`用
 * @author gcc
 */
interface SaveView<Entity>: BaseController<Entity> {

    /**
     * 新增页面
     *
     * @param model [Model]
     * @param entity 实体类 路由后面携带的参数会被封装到实体类里面。当然得是实体类中的字段才行
     * @param request 请求对象
     */
    @ApiIgnore
    @GetMapping(value = ["/save", "/add"])
    fun saveView(model: Model, entity: Entity?, request: HttpServletRequest): String {
        // 进入新增页面前的一些处理
        beforeSaveView(model, entity, request)
        return goView("save.html")
    }

    /**
     * 进入新增页面前的一些处理
     *
     * 说明：
     * 你可以在这里向新增页面传参
     *
     * @param model [Model]
     * @param entity 实体类 路由后面携带的参数会被封装到实体类里面。当然得是实体类中的字段才行
     * @param request 请求对象
     */
    fun beforeSaveView(model: Model, entity: Entity?, request: HttpServletRequest) { }

}
