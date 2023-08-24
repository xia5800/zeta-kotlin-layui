package com.zeta.system.model.dto.sysMenu

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDateTime

/**
 * 菜单 详情
 *
 * @author AutoGenerator
 * @date 2022-04-24 17:45:03
 */
@ApiModel(description = "菜单详情")
data class SysMenuDTO(

    /** id */
    @ApiModelProperty(value = "id")
    var id: Long? = null,

    /** 创建时间 */
    @ApiModelProperty(value = "创建时间")
    var createTime: LocalDateTime? = null,

    /** 创建人 */
    @ApiModelProperty(value = "创建人")
    var createdBy: Long? = null,

    /** 修改时间 */
    @ApiModelProperty(value = "修改时间")
    var updateTime: LocalDateTime? = null,

    /** 修改人 */
    @ApiModelProperty(value = "修改人")
    var updatedBy: Long? = null,

    /** 名称 */
    @ApiModelProperty(value = "名称")
    var title: String? = null,

    /** 父级id */
    @ApiModelProperty(value = "父级id")
    var parentId: Long? = null,

    /** 排序 */
    @ApiModelProperty(value = "排序")
    var sortValue: Int? = null,

    /** 图标 */
    @ApiModelProperty(value = "图标")
    var icon: String? = null,

    /** 权限标识 */
    @ApiModelProperty(value = "权限标识")
    var authority: String? = null,

    /** 菜单类型 */
    @ApiModelProperty(value = "菜单类型")
    var type: Int? = null,

    /** 逻辑删除字段 */
    @ApiModelProperty(value = "逻辑删除字段")
    var deleted: Boolean? = null,

    /** 打开方式 _blank, _iframe */
    @ApiModelProperty(value = "打开方式")
    var openType: String? = null,

    /** 外链地址 */
    @ApiModelProperty(value = "外链地址")
    var href: String? = null,
)
