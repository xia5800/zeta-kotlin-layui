package org.zetaframework.core.beetl

import org.beetl.core.resource.ClasspathResourceLoader
import org.beetl.ext.spring.BeetlGroupUtilConfiguration
import org.beetl.ext.spring.BeetlSpringViewResolver
import org.springframework.beans.factory.annotation.Qualifier
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
@Configuration
@EnableConfigurationProperties(BeetlProperties::class)
class BeetlConfiguration(private val beetlProperties: BeetlProperties) {

    /**
     * beetl模板配置
     */
    @Bean("beetlConfig")
    fun beetlGroupUtilConfiguration(): BeetlGroupUtilConfiguration {
        return BeetlGroupUtilConfiguration().apply {
            // 修改模板基础配置 参考：http://www.ibeetl.com/guide/#/beetl/basic?id=%e6%a8%a1%e6%9d%bf%e5%9f%ba%e7%a1%80%e9%85%8d%e7%bd%ae
            val extProperties = Properties().apply {
                // 根据系统配置，自动开关dev模式
                put("RESOURCE.autoCheck", if (beetlProperties.dev) "true" else "false")
                // 防止和layui的#冲突，将beetl的#替换成:
                put("HTML_TAG_FLAG", ":")
                // 在页面输出错误提示信息
                if (beetlProperties.dev) {
                    put("ERROR_HANDLER", "org.beetl.ext.web.WebErrorHandler")
                }
            }
            this.setConfigProperties(extProperties)

            // 获取Spring Boot 的ClassLoader
            var loader = Thread.currentThread().contextClassLoader
            if (loader == null) { loader = BeetlConfiguration::class.java.classLoader }
            this.setResourceLoader(ClasspathResourceLoader(loader, beetlProperties.templatesPath))

            this.init()

            // 如果使用了优化编译器，涉及到字节码操作，需要添加ClassLoader
            this.groupTemplate.classLoader = loader

            // 添加自定义模板函数
            this.groupTemplate.registerFunction("parseBoolean", ParseBooleanFunction())
            this.groupTemplate.registerFunctionPackage("sa", SaFunctionPackage())

            // 添加自定义模板标签
            this.groupTemplate.registerTag("include", org.beetl.ext.tag.html.IncludeResourceHtmlTag::class.java)
            this.groupTemplate.registerTag("layout", org.beetl.ext.tag.html.LayoutResourceHtmlTag::class.java)
            this.groupTemplate.registerTag("set", org.beetl.ext.tag.html.SetHtmlTag::class.java)
            this.groupTemplate.registerTag("if", org.beetl.ext.tag.html.IfHtmlTag::class.java)
            this.groupTemplate.registerTag("for", org.beetl.ext.tag.html.ForeachHtmlTag::class.java)
        }
    }

    /**
     * 配置beetl视图解析器
     *
     * @param
     */
    @Bean("beetlViewResolver")
    fun beetlViewResolver(@Qualifier("beetlConfig") bguc: BeetlGroupUtilConfiguration?): BeetlSpringViewResolver {
        return BeetlSpringViewResolver().apply {
            this.setContentType("text/html;charset=UTF-8")
            // 影响到代码中的 goView("index.html")的后缀 ".html".
            this.setViewNames("*.${beetlProperties.suffix}")
            this.order = 0
            this.config = bguc
        }
    }

}
