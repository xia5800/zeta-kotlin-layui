package com.zeta.system.model.param

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.NotBlank

/**
 * 系统文件 上传参数
 *
 * @author gcc
 */
@ApiModel(description = "系统文件上传参数")
data class SysFileUploadParam(

    /** 文件base64 */
    @ApiModelProperty(value = "文件base64", required = true)
    @get:NotBlank(message = "base64值不能为空")
    var base64: String? = null,

    /** 业务类型 */
    @ApiModelProperty(value = "业务类型", required = false)
    var bizType: String? = null,

    )
