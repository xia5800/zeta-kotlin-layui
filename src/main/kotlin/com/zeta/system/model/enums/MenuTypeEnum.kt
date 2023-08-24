package com.zeta.system.model.enums

import io.swagger.annotations.ApiModel

/**
 * 菜单类型
 * @author gcc
 */
@ApiModel(description = "菜单类型 枚举")
enum class MenuTypeEnum(val code: Int) {
    /** 目录 */
    DIRECTORY(0),

    /** 菜单 */
    MENU(1),

    /** 权限 */
    RESOURCE(2);
}
