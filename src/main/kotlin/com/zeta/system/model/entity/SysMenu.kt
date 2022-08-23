package com.zeta.system.model.entity

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableLogic
import com.baomidou.mybatisplus.annotation.TableName
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.zetaframework.base.entity.TreeEntity
import javax.validation.constraints.NotNull

/**
 * <p>
 * 菜单
 * </p>
 *
 * @author AutoGenerator
 * @date 2022-04-24 17:45:03
 */
@ApiModel(description = "菜单")
@TableName(value = "sys_menu")
class SysMenu: TreeEntity<SysMenu, Long>() {

    /** 图标 */
    @ApiModelProperty(value = "图标")
    @TableField(value = "icon")
    var icon: String? = null

    /** 权限标识 */
    @ApiModelProperty(value = "权限标识")
    @TableField(value = "authority")
    var authority: String? = null

    /** 菜单类型 */
    @ApiModelProperty(value = "菜单类型")
    @get:NotNull(message = "菜单类型不能为空")
    @TableField(value = "type")
    var type: Int? = null

    /** 逻辑删除字段 */
    @ApiModelProperty(value = "逻辑删除字段")
    @TableLogic
    var deleted: Boolean? = null

    /** 打开方式 _blank, _iframe */
    @ApiModelProperty(value = "打开方式")
    @TableField(value = "open_type")
    var openType: String? = null

    /** 外链地址 */
    @ApiModelProperty(value = "外链地址")
    @TableField(value = "href")
    var href: String? = null

    override fun toString(): String {
        return "SysMenu(id=$id, createTime=$createTime, createdBy=$createdBy, updateTime=$updateTime, updatedBy=$updatedBy, title=$title, parentId=$parentId, sortValue=$sortValue, icon=$icon, authority=$authority, type=$type, deleted=$deleted, href=$href)"
    }

}
