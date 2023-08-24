package com.zeta.system.model.dto.sysMenu

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

/**
 * 新增 菜单
 *
 * @author AutoGenerator
 * @date 2022-04-24 17:45:03
 */
@ApiModel(description = "新增菜单")
data class SysMenuSaveDTO(

    /** 名称 */
    @ApiModelProperty(value = "名称", required = true)
    @get:NotBlank(message = "菜单名称不能为空")
    @get:Size(max = 32, message = "菜单名称长度不能超过32")
    var title: String? = null,

    /** 父级id */
    @ApiModelProperty(value = "父级id", required = true)
    @get:NotNull(message = "父级id不能为空")
    var parentId: Long? = null,

    /** 排序 */
    @ApiModelProperty(value = "排序", required = false)
    var sortValue: Int? = null,

    /** 图标 */
    @ApiModelProperty(value = "图标", required = false)
    var icon: String? = null,

    /** 权限标识 */
    @ApiModelProperty(value = "权限标识", required = false)
    var authority: String? = null,

    /** 菜单类型 */
    @ApiModelProperty(value = "菜单类型", required = false)
    var type: Int? = null,

    /** 打开方式 _blank, _iframe */
    @ApiModelProperty(value = "打开方式", required = false)
    var openType: String? = null,

    /** 外链地址 */
    @ApiModelProperty(value = "外链地址", required = false)
    var href: String? = null,
)
