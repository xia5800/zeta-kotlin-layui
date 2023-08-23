package org.zetaframework.base.controller.view

import cn.hutool.core.text.StrPool
import org.zetaframework.base.controller.SuperBaseController


/**
 * 默认视图
 *
 * 说明：
 * 适用于“有的控制层不需要跳转页面，但是实现了`SuperBaseController`接口不得不重写getModulePath()方法”情况。
 * 例如： MainController
 *
 * @author gcc
 */
interface DefaultView: SuperBaseController {

    /**
     * 当前模块路径
     */
    override fun getModulePath(): String {
        return StrPool.SLASH
    }
}
