package org.zetaframework.core.beetl.function

import cn.hutool.core.util.BooleanUtil
import org.beetl.core.Context
import org.beetl.core.Function

/**
 * 自定义beetl函数parseBoolean()。
 *
 * 说明：
 * 1.将对象转换成true或false返回
 * 2."true", "yes", "y", "t", "ok", "1", "on", "是", "对", "真", "對", "√" 的结果均为true。
 *   其它结果为false
 *
 * 使用方式：
 * ```
 * <%
 *  parseBoolean("true"); // 结果为true
 *  parseBoolean("false"); // 结果为false
 *  parseBoolean(""); // 结果为false
 *  parseBoolean("123"); // 结果为false
 *  parseBoolean("false", "123"); // 抛出异常
 *  parseBoolean(null); // 抛出异常
 * %>
 * ```
 * @author gcc
 */
class ParseBooleanFunction: Function {

    /**
     * @param paras beetl传递的参数
     */
    override fun call(paras: Array<out Any?>, ctx: Context): Any? {
        if (paras.size != 1) {
            throw RuntimeException("参数错误，期望参数：Object")
        }

        if (paras[0] == null) {
            throw RuntimeException("参数错误，值不能为null")
        }

        return if (paras[0] is Boolean) paras[0] else BooleanUtil.toBoolean(paras[0].toString())
    }
}
