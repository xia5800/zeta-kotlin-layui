package org.zetaframework.base.controller.view

import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.zetaframework.base.controller.BaseController
import org.zetaframework.base.controller.goView
import springfox.documentation.annotations.ApiIgnore
import javax.servlet.http.HttpServletRequest

/**
 * 编辑页视图
 *
 * 说明:
 * 跳转到`modules/edit.html`用
 * @author gcc
 */
interface UpdateView<Entity>: BaseController<Entity> {

    /**
     * 修改页面
     *
     * @param model [Model]
     * @param entity 实体类 路由后面携带的参数会被封装到实体类里面。当然得是实体类中的字段才行
     * @param request 请求对象
     */
    @ApiIgnore
    @GetMapping(value = ["/update", "/edit"])
    fun updateView(model: Model, entity: Entity?, request: HttpServletRequest): String {
        // 进入修改页面前的一些处理
        beforeUpdateView(model, entity, request)
        return goView("edit.html")
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
    fun beforeUpdateView(model: Model, entity: Entity?, request: HttpServletRequest) { }

}
