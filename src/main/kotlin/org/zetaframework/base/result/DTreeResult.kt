package org.zetaframework.base.result

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.zetaframework.base.entity.TreeEntity
import org.zetaframework.core.enums.ErrorCodeEnum

/**
 * 前端DTree组件返回结果
 *
 * @author gcc
 */
@ApiModel(description = "DTree返回结果")
class DTreeResult private constructor(){

    /** 状态码 */
    @ApiModelProperty(value = "状态码")
    var code: Int? = null

    /** 状态信息 */
    @ApiModelProperty(value = "状态信息")
    var msg: String? = null

    /** 状态, 不使用dataStyle: layuiStyle的时候生效 */
    @ApiModelProperty(value = "状态", notes = "不使用dataStyle: layuiStyle的时候生效")
    var status: Status? = null

    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    var data: List<DTree>? = mutableListOf()


    constructor(data: List<DTree>?): this(ErrorCodeEnum.OK, data) {
        this.data = data
    }

    constructor(errorCodeEnum: ErrorCodeEnum, data: List<DTree>? = mutableListOf()): this() {
        this.code = errorCodeEnum.code
        this.msg = errorCodeEnum.msg
        this.data = data
    }

    constructor(code: Int, msg: String, data: List<DTree>) : this() {
        this.code = code
        this.msg = msg
        this.data = data
    }


    /** dTree状态类 */
    data class Status(
        /** 状态码 */
        @ApiModelProperty(value = "状态码")
        var code: Int = 200,

        /** 信息标识 */
        @ApiModelProperty(value = "信息标识")
        var message: String = "success"
    )
}

/**
 * DTree树类
 *
 * @author gcc
 */
@ApiModel(description = "DTree树类")
class DTree : TreeEntity<DTree, Long>() {

    /** 是否展开节点 */
    @ApiModelProperty(value = "是否展开节点")
    var spread: Boolean? = false

    /** 是否最后一级节点 */
    @ApiModelProperty(value = "是否最后一级节点")
    var last: Boolean? = false

    /** 是否隐藏 */
    @ApiModelProperty(value = "是否隐藏")
    var hide: Boolean? = false

    /** 是否禁用 */
    @ApiModelProperty(value = "是否禁用")
    var disabled: Boolean? = false

    /** 自定义图标class */
    @ApiModelProperty(value = "自定义图标class")
    var iconClass: String? = null

    /** 表示用户自定义需要存储在树节点中的数据 */
    @ApiModelProperty(value = "用户自定义需要存储在树节点中的数据")
    var basicData: Any? = null

    /** 复选框集合 */
    @ApiModelProperty(value = "复选框集合")
    var checkArr: List<CheckArr>? = mutableListOf()


    /**
     * 复选框设计类
     */
    data class CheckArr(
        /** 复选框下标。 第1个下标为0 */
        @ApiModelProperty(value = "复选框下标", example = "0", notes = "第1个下标为0")
        var type: String = "0",

        /** 是否选中。 0不选中,1选中 */
        @ApiModelProperty(value = "是否选中", example = "0", notes = "0不选中,1选中")
        var checked: String = "0"
    )
}
