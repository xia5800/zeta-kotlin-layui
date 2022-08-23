package org.zetaframework.core.beetl.properties

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * beetl配置参数
 * @author gcc
 */
@ConfigurationProperties(prefix = "zeta.beetl")
data class BeetlProperties(
    /** 模板根目录 */
    var templatesPath: String = "templates",

    /** 模板后缀 */
    var suffix: String = "html",

    /** 开发模式 默认：false */
    var dev: Boolean = false,
)
