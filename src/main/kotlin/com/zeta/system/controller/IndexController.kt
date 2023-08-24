package com.zeta.system.controller

import cn.dev33.satoken.stp.StpUtil
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.zetaframework.base.controller.SuperBaseController
import org.zetaframework.base.controller.view.DefaultView
import springfox.documentation.annotations.ApiIgnore

/**
 * 通用页面
 * @author gcc
 */
@ApiIgnore // swagger不显示这个类的接口
@Controller
class IndexController : SuperBaseController, DefaultView, ErrorController {

    /**
     * 登录页面
     * @param model Model
     */
    @GetMapping("/login")
    fun loginView(model: Model): String {
        return if (StpUtil.isLogin()) redirect("/index") else "/login.html"
    }

    /**
     * 首页
     * @param model Model
     */
    @GetMapping(value = ["/", "/index"])
    fun indexView(model: Model): String = "/index.html"

    /**
     * 分析页
     * @param model Model
     */
    @GetMapping("/dashboard")
    fun dashboard(model: Model): String = "/dashboard/index.html"

    /**
     * 工作台
     */
    @GetMapping("/dashboard/workbench")
    fun workbench(model: Model): String = "/dashboard/workbench.html"

    /**
     * 无权限页
     * @param model Model
     */
    @GetMapping("/error/403")
    fun noPermission(): String = "/error/403.html"

    /**
     * 404页
     * @param model Model
     */
    @RequestMapping("/error")
    fun notFound(): String = "/error/404.html"

    /**
     * 异常页
     * @param model Model
     */
    @GetMapping("/error/500")
    fun onException(): String = "/error/500.html"

}
