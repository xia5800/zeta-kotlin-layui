package org.zetaframework.base.param

import cn.hutool.core.util.StrUtil
import com.baomidou.mybatisplus.core.metadata.IPage
import com.baomidou.mybatisplus.core.metadata.OrderItem
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

/**
 * 分页查询参数
 *
 * @author gcc
 */
@ApiModel(description = "分页查询参数")
class PageParam private constructor() {
    /** 当前页 */
    @ApiModelProperty(value = "当前页", example = "1", required = true)
    var page: Long = 1

    /** 每页显示条数 */
    @ApiModelProperty(value = "每页显示条数", example = "10", required = true)
    var limit: Long = 10

    /** 排序字段 */
    @ApiModelProperty(
        value = "排序字段",
        allowableValues = "id,createTime,updateTime",
        example = "id",
        required = false
    )
    var sort: String? = null

    /** 排序规则 */
    @ApiModelProperty(value = "排序规则", allowableValues = "desc,asc", example = "desc", required = false)
    var order: String? = null


    constructor(page: Long, limit: Long) : this() {
        this.page = page
        this.limit = limit
    }


    /**
     * 构建分页对象
     * @return IPage<E>
     */
    fun <E> buildPage(): IPage<E> {
        val page: Page<E> = Page<E>(this.page, this.limit)

        // 判断是否有排序
        if (StrUtil.isBlank(this.sort)) {
            return page
        }

        // 处理排序
        val orders: MutableList<OrderItem> = mutableListOf()
        val sortArr = StrUtil.splitToArray(this.sort, StrUtil.COMMA)
        val orderArr = StrUtil.splitToArray(this.order, StrUtil.COMMA)
        for (i in sortArr.indices) {
            // bug fix: 驼峰转下划线  说明：忘记处理了orz --by gcc
            val sortField = StrUtil.toUnderlineCase(sortArr[i])
            orders.add(
                if (StrUtil.equalsAny(orderArr[i], "asc", "ascending")) OrderItem.asc(sortField) else OrderItem.desc(
                    sortField
                )
            )
        }
        page.orders = orders
        return page
    }


    /**
     * 设置默认排序方式
     *
     * 说明：
     * 不用担心驼峰不驼峰，buildPage()方法会将驼峰转下划线
     *
     * 使用方式：
     * ```
     *      val pageParam = PageParam(1, 10)
     *      // id倒序
     *      pageParam.setDefaultOrder(descs = mutableListOf("id"))
     *      // id倒序，sex正序
     *      pageParam.setDefaultOrder(mutableListOf("sex"), mutableListOf("id"))
     *      // username、sex正序， createTime倒序
     *      pageParam.setDefaultOrder(mutableListOf("username", "sex"), mutableListOf("createTime"))
     * ```
     *
     * @param ascs 正序排序的字段  例如：createTime、create_time 均可
     * @param descs 倒序排序的字段
     */
    fun setDefaultOrder(ascs: List<String>? = null, descs: List<String>? = null) {
        if (ascs == null && descs == null) {
            return
        }

        val sortList: MutableList<String> = mutableListOf()
        val orderList: MutableList<String> = mutableListOf()

        // 如果有正序排序的字段，且字段不为空字符串。
        ascs?.filterNot { StrUtil.isBlank(it) }?.forEach {
            sortList.add(it)
            orderList.add("asc")
        }
        // 如果有倒序排序的字段，且字段不为空字符串。
        descs?.filterNot { StrUtil.isBlank(it) }?.forEach {
            sortList.add(it)
            orderList.add("desc")
        }

        // 得到排序字段  "id,createTime,username"
        this.sort = sortList.joinToString(",")
        // 得到排序规则  "asc,asc,desc"
        this.order = orderList.joinToString(",")
    }

    /**
     * 设置默认倒序排序的字段
     *
     * @param field 倒序排序的字段  例如：createTime、create_time 均可
     */
    fun setDefaultDesc(field: String) {
        if (field === "") {
            return
        }
        setDefaultOrder(descs = mutableListOf(field))
    }
}
