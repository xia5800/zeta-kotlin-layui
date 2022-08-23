package org.zetaframework.base.result

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.zetaframework.base.entity.TreeEntity
import org.zetaframework.core.enums.ErrorCodeEnum

/**
 * 前端XmSelect组件返回结果
 *
 * @author gcc
 */
@ApiModel(description = "XmSelect返回结果")
class XmSelectResult private constructor() {

    /** 状态码 */
    @ApiModelProperty(value = "状态码")
    var code: Int? = null

    /** 状态信息 */
    @ApiModelProperty(value = "状态信息")
    var msg: String? = null

    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    var data: List<XmSelect>? = mutableListOf()


    constructor(data: List<XmSelect>?): this(ErrorCodeEnum.OK, data) {
        this.data = data
    }

    constructor(errorCodeEnum: ErrorCodeEnum, data: List<XmSelect>? = mutableListOf()): this() {
        this.code = errorCodeEnum.code
        this.msg = errorCodeEnum.msg
        this.data = data
    }

    constructor(code: Int, msg: String, data: List<XmSelect>) : this() {
        this.code = code
        this.msg = msg
        this.data = data
    }

}

/**
 * XmSelect
 *
 * @author gcc
 */
@ApiModel(description = "XmSelect类")
class XmSelect: TreeEntity<XmSelect, Long>() {

    /** 名称 */
    @ApiModelProperty(value = "名称")
    var name: String? = null
    get() = this.title

    /** 值 */
    @ApiModelProperty(value = "值")
    var value: Long? = null
    get() = this.id

    /** 是否选中 */
    @ApiModelProperty(value = "是否选中")
    var selected: Boolean? = false

    /** 是否禁用 */
    @ApiModelProperty(value = "是否禁用")
    var disabled: Boolean? = false
}
