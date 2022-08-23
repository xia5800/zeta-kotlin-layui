package com.zeta.system.model.dto.sysMenu

import com.fasterxml.jackson.annotation.JsonInclude
import com.zeta.system.model.entity.SysMenu
import io.swagger.annotations.ApiModelProperty
import org.zetaframework.base.entity.TreeEntity

/**
 * 菜单xmSelect选择数据
 *
 * @author gcc
 */
@Deprecated("不再使用")
@JsonInclude(JsonInclude.Include.NON_NULL)
class MenuSelectData : TreeEntity<MenuSelectData, Long>() {

    /** 名称 */
    @ApiModelProperty(value = "名称")
    var name: String? = null

    /** 值 */
    @ApiModelProperty(value = "值")
    var value: Long? = null

    /** 是否选中 */
    @ApiModelProperty(value = "是否选中")
    var selected: Boolean? = null

    companion object {
        fun convert(data: SysMenu): MenuSelectData {
            return MenuSelectData().apply {
                id = data.id
                parentId = data.parentId
                name = data.title
                value = data.id
                selected = false
            }
        }

        fun convert(data: SysMenu, pid: Long): MenuSelectData {
            return MenuSelectData().apply {
                id = data.id
                parentId = data.parentId
                name = data.title
                value = data.id
                selected = pid == data.id
            }
        }
    }
}
