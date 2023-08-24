package org.zetaframework.base.result

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.zetaframework.core.enums.ErrorCodeEnum

/**
 * 分页查询返回结果
 *
 * @author gcc
 */
@ApiModel(description = "分页查询返回结果")
class PageResult<T> private constructor() {

    /** 状态码 */
    @ApiModelProperty(value = "状态码")
    var code: Int? = null

    /** 状态信息 */
    @ApiModelProperty(value = "状态信息")
    var msg: String? = null

    /** 当前页数据 */
    @ApiModelProperty(value = "当前页数据")
    var data: List<T>? = mutableListOf()

    /** 总数量 */
    @ApiModelProperty(value = "总数量")
    var count: Int? = null


    constructor(data: List<T>?, count: Int?) : this(ErrorCodeEnum.OK, data, count) {
        this.data = data
        this.count = count
    }

    constructor(errorCodeEnum: ErrorCodeEnum, data: List<T>? = mutableListOf(), count: Int? = 0) : this() {
        this.code = errorCodeEnum.code
        this.msg = errorCodeEnum.msg
        this.data = data
        this.count = count
    }

    constructor(code: Int, msg: String, data: List<T>, count: Int? = 0) : this() {
        this.code = code
        this.msg = msg
        this.data = data
        this.count = count
    }

}
