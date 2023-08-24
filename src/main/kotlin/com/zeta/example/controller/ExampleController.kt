package com.zeta.example.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.zetaframework.base.controller.SuperBaseController
import org.zetaframework.base.controller.goView
import springfox.documentation.annotations.ApiIgnore

/**
 * 案例
 *
 * @author gcc
 */
@ApiIgnore // swagger不显示这个类的接口
@Controller
@RequestMapping("/example")
class ExampleController : SuperBaseController {

    /**
     * 当前模块路径
     */
    override fun getModulePath(): String {
        return "/example"
    }

    /**
     * 前往document相关页面
     *
     * 例如：
     * url -> /example/document/area
     * 对应的页面地址 -> /example/document/area.html
     *
     * url -> /example/document/button
     * 对应的页面地址 -> /example/document/button.html
     */
    @GetMapping("/document/{name}")
    fun document(@PathVariable("name") viewName: String): String {
        return goView("document/${viewName}.html")
    }

    /**
     * 前往result相关页面
     */
    @GetMapping("/result/{name}")
    fun result(@PathVariable("name") viewName: String): String {
        return goView("result/${viewName}.html")
    }

    /**
     * 前往echarts相关页面
     */
    @GetMapping("/echarts/{name}")
    fun echarts(@PathVariable("name") viewName: String): String {
        return goView("echarts/${viewName}.html")
    }

}
