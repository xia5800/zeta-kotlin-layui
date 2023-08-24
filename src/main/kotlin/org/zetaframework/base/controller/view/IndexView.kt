package org.zetaframework.base.controller.view

import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.zetaframework.base.controller.BaseController
import org.zetaframework.base.controller.goView
import springfox.documentation.annotations.ApiIgnore
import javax.servlet.http.HttpServletRequest

/**
 * 主页视图
 *
 * 说明:
 * 跳转到`modules/index.html`用
 * @author gcc
 */
interface IndexView<Entity> : BaseController<Entity> {

    /**
     * 主页
     *
     * @param model [Model]
     * @param entity 实体类 路由后面携带的参数会被封装到实体类里面。当然得是实体类中的字段才行
     * @param request 请求对象
     */
    @ApiIgnore
    @GetMapping("/index")
    fun indexView(model: Model, entity: Entity?, request: HttpServletRequest): String {
        // 进入主页前的一些处理
        beforeIndexView(model, entity, request)
        return goView("index.html")
    }

    /**
     * 进入主页前的一些处理
     *
     * 说明：
     * 你可以在这里向主页传参
     *
     * @param model [Model]
     * @param entity 实体类 路由后面携带的参数会被封装到实体类里面。当然得是实体类中的字段才行
     * @param request 请求对象
     */
    fun beforeIndexView(model: Model, entity: Entity?, request: HttpServletRequest) {}


}
