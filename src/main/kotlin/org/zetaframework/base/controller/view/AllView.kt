package org.zetaframework.base.controller.view

/**
 * 包含了“首页视图、新增页视图、编辑页视图”的视图
 *
 * @author gcc
 */
interface AllView<Entity> : IndexView<Entity>, SaveView<Entity>, UpdateView<Entity> {
}
