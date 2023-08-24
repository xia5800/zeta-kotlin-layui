package com.zeta.system.model.param

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.NotBlank

/**
 * 登录参数
 * @author gcc
 */
@ApiModel(description = "登录参数")
data class LoginParam(

    /** 账号 */
    @ApiModelProperty(value = "账号", required = true)
    @get:NotBlank(message = "账号不能为空")
    var account: String? = null,

    /** 密码 */
    @ApiModelProperty(value = "密码", required = true)
    @get:NotBlank(message = "密码不能为空")
    var password: String? = null,

    /** 验证码 */
    @ApiModelProperty(value = "验证码", required = true)
    @get:NotBlank(message = "验证码不能为空")
    val code: String? = null,

    /** 记住密码 */
    @ApiModelProperty(value = "记住密码", required = true)
    val remember: Boolean? = null
)
