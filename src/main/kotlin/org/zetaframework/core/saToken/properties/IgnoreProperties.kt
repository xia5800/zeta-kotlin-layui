package org.zetaframework.core.saToken.properties

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * security忽略鉴权配置类
 * @author gcc
 */
@ConfigurationProperties(prefix = "zeta.security.ignore")
class IgnoreProperties {

    /** 静态资源、swagger、druid、layui相关 */
    var staticUrl: MutableList<String> = mutableListOf(
        // 以下是swagger相关的放行地址
        "/**/*.html",
        "/**/*.css",
        "/**/*.js",
        "/**/*.ico",
        "/**/*.jpg",
        "/**/*.png",
        "/**/*.gif",
        "/**/api-docs/**",
        "/**/api-docs-ext/**",
        "/**/swagger-resources/**",
        "/**/webjars/**",
        // druid监控页面放行
        "/druid/**",
        // layui前端相关放行地址
        "/favicon.ico",
        "/assets/**",
    )

    /** 基础忽略鉴权地址 */
    var baseUrl: MutableList<String> = mutableListOf(
        "/error",
        "/login",
        // websocket放行地址
        "/ws/**",
    )

    /** 忽略鉴权的地址 */
    var ignoreUrl: MutableList<String> = mutableListOf("/**/noToken/**")


    /**
     * 获取saToken放行路由
     */
    fun getNotMatchUrl(): MutableList<String> {
        return mutableListOf<String>().apply {
            addAll(staticUrl)
            addAll(baseUrl)
            addAll(ignoreUrl)
        }
    }
}
