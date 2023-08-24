package com.zeta.system.model.entity

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableLogic
import com.baomidou.mybatisplus.annotation.TableName
import com.fasterxml.jackson.annotation.JsonIgnore
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.zetaframework.base.entity.TreeEntity
import javax.validation.constraints.NotNull

/**
 * 菜单
 *
 * @author AutoGenerator
 * @date 2022-04-24 17:45:03
 */
@ApiModel(description = "菜单")
@TableName(value = "sys_menu")
class SysMenu : TreeEntity<SysMenu, Long>() {

    /** 图标 */
    @ApiModelProperty(value = "图标", required = false)
    @TableField(value = "icon")
    var icon: String? = null

    /** 权限标识 */
    @ApiModelProperty(value = "权限标识", required = false)
    @TableField(value = "authority")
    var authority: String? = null

    /** 菜单类型 */
    @ApiModelProperty(value = "菜单类型", required = true)
    @get:NotNull(message = "菜单类型不能为空")
    @TableField(value = "type")
    var type: Int? = null

    /** 逻辑删除字段 */
    @JsonIgnore
    @ApiModelProperty(value = "逻辑删除字段", hidden = true, required = true)
    @TableLogic
    var deleted: Boolean? = null

    /** 打开方式 _blank, _iframe */
    @ApiModelProperty(value = "打开方式", required = false)
    @TableField(value = "open_type")
    var openType: String? = null

    /** 外链地址 */
    @ApiModelProperty(value = "外链地址", required = false)
    @TableField(value = "href")
    var href: String? = null

    override fun toString(): String {
        return "SysMenu(id=$id, createTime=$createTime, createdBy=$createdBy, updateTime=$updateTime, updatedBy=$updatedBy, title=$title, parentId=$parentId, sortValue=$sortValue, icon=$icon, authority=$authority, type=$type, deleted=$deleted, href=$href)"
    }

}
