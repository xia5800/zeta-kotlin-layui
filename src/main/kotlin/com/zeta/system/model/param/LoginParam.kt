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
    @ApiModelProperty("账号")
    @get:NotBlank(message = "账号不能为空")
    var account: String? = null,

    /** 密码 */
    @ApiModelProperty("密码")
    @get:NotBlank(message = "密码不能为空")
    var password: String? = null,

    /** 验证码 */
    @ApiModelProperty("验证码")
    @get:NotBlank(message = "验证码不能为空")
    val code: String? = null,

    /** 记住密码 */
    @ApiModelProperty("记住密码")
    val remember: Boolean? = null
)
