package com.zeta.system.model.param

import io.swagger.annotations.ApiModel
import javax.validation.constraints.NotBlank

/**
 * <p>
 * 系统文件 上传参数
 * </p>
 *
 * @author gcc
 */
@ApiModel(description = "系统文件上传参数")
data class SysFileUploadParam(

    /** 文件base64 */
    @get:NotBlank(message = "base64值不能为空")
    var base64: String? = null,

    /** 业务类型 */
    var bizType: String? = null,

)
