package org.zetaframework.core.beetl

import org.beetl.core.GroupTemplate
import org.beetl.ext.spring.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.zetaframework.core.beetl.function.ParseBooleanFunction
import org.zetaframework.core.beetl.function.SaFunctionPackage
import org.zetaframework.core.beetl.properties.BeetlProperties
import java.util.*


/**
 * Beetl模板配置
 *
 * @author gcc
 */
@EnableBeetl
@Configuration
@EnableConfigurationProperties(BeetlProperties::class)
class BeetlConfiguration() {

    @Value("\${beetl-beetlsql.dev:true}")
    var dev = false

    /**
     * 自定义beetl模板配置
     *
     * 说明：
     * 1.参考[3.5.2 Spring Boot 2](https://www.kancloud.cn/xiandafu/beetl3_guide/2138987)
     * 2.原来需要配置的两个bean [BeetlGroupUtilConfiguration]和[BeetlSpringViewResolver] 已经在
     * org.beetl.ext.spring.[BeetlTemplateConfig]里面配置过了
     */
    @Bean
    fun beetlTemplateCustomize(): BeetlTemplateCustomize {
        return BeetlTemplateCustomize { groupTemplate: GroupTemplate ->
            // 修改模板基础配置 参考：https://www.kancloud.cn/xiandafu/beetl3_guide/2138945
            groupTemplate.conf?.let { conf ->
                // 防止和layui的#冲突，将beetl的"#"替换成":"
                conf.htmlTagFlag = ":"
                // Configuration类的resetHtmlTag方法是private的
                // 因此还要手动修改
                conf.htmlTagStart = "<:"
                conf.htmlTagEnd = "</:"
                // 还要重新配置HtmlTagHolder。不然上面的修改不生效
                conf.build()

                // 开发环境：在页面输出错误提示信息
                if (dev) conf.errorHandlerClass = "org.beetl.ext.web.WebErrorHandler"

                // 将修改后的Configuration对象从新赋值到groupTemplate
                groupTemplate.conf = conf
            }

            // 添加自定义模板函数
            groupTemplate.registerFunction("parseBoolean", ParseBooleanFunction())
            groupTemplate.registerFunctionPackage("sa", SaFunctionPackage())

            // 添加自定义模板标签
            groupTemplate.registerTag("include", org.beetl.ext.tag.html.IncludeResourceHtmlTag::class.java)
            groupTemplate.registerTag("layout", org.beetl.ext.tag.html.LayoutResourceHtmlTag::class.java)
            groupTemplate.registerTag("set", org.beetl.ext.tag.html.SetHtmlTag::class.java)
            groupTemplate.registerTag("if", org.beetl.ext.tag.html.IfHtmlTag::class.java)
            groupTemplate.registerTag("for", org.beetl.ext.tag.html.ForeachHtmlTag::class.java)
        }
    }


}
