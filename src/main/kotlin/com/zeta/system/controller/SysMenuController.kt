package com.zeta.system.controller

import cn.hutool.core.bean.BeanUtil
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport
import com.zeta.system.model.dto.sysMenu.SysMenuSaveDTO
import com.zeta.system.model.dto.sysMenu.SysMenuUpdateDTO
import com.zeta.system.model.entity.SysMenu
import com.zeta.system.model.enums.MenuTypeEnum
import com.zeta.system.model.param.SysMenuQueryParam
import com.zeta.system.service.ISysMenuService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.zetaframework.base.controller.SuperAllViewController
import org.zetaframework.base.result.PageResult
import org.zetaframework.base.result.XmSelect
import org.zetaframework.base.result.XmSelectResult
import org.zetaframework.core.exception.BusinessException
import org.zetaframework.core.saToken.annotation.PreAuth
import org.zetaframework.core.saToken.annotation.PreCheckPermission
import org.zetaframework.core.utils.TreeUtil
import javax.servlet.http.HttpServletRequest

/**
 * 菜单 前端控制器
 *
 * @author AutoGenerator
 * @date 2021-12-30 15:24:03
 */
@Api(tags = ["菜单管理"])
@PreAuth(replace = "sys:menu")
@Controller
@RequestMapping("/system/menu")
class SysMenuController : SuperAllViewController<ISysMenuService, Long, SysMenu, SysMenuQueryParam, SysMenuSaveDTO, SysMenuUpdateDTO>() {

    /**
     * 当前模块路径
     */
    override fun getModulePath(): String {
        return "/system/menu"
    }

    /**
     * 进入修改页面前的一些处理
     *
     * 说明：
     * 你可以在这里向修改页面传参
     *
     * @param model [Model]
     * @param entity 实体类 路由后面携带的参数会被封装到实体类里面。当然得是实体类中的字段才行
     * @param request 请求对象
     */
    override fun beforeUpdateView(model: Model, entity: SysMenu?, request: HttpServletRequest) {
        // 查询菜单
        val menu = entity?.id.let {
            service.getById(it)
        } ?: throw BusinessException("菜单不存在")
        model.addAttribute("menu", menu)
    }

    /**
     * 自定义批量查询
     *
     * @param param QueryParam
     * @return MutableList<Entity>
     */
    override fun handlerBatchQuery(param: SysMenuQueryParam): MutableList<SysMenu> {
        val entity = BeanUtil.toBean(param, getEntityClass())

        // 构造查询请求
        val queryWrapper = KtQueryWrapper(entity).orderByAsc(SysMenu::sortValue)
        if (param.excludeAuthority != null && param.excludeAuthority!!) {
            queryWrapper.`in`(SysMenu::type, mutableListOf(MenuTypeEnum.DIRECTORY.code, MenuTypeEnum.MENU.code))
        }

        // 查询菜单
        val list = getBaseService().list(queryWrapper)

        // 处理批量查询数据
        handlerBatchData(list)
        return list
    }

    /**
     * 查询菜单树
     *
     * @param param SysMenuQueryParam
     * @return List<Menu>
     */
    @PreCheckPermission(value = ["{}:view"])
    @ApiOperationSupport(ignoreParameters = ["children"])
    @ApiOperation(value = "查询菜单树")
    @ResponseBody
    @GetMapping("/tree")
    fun tree(param: SysMenuQueryParam): PageResult<SysMenu> {
        // 条件查询菜单
        val menuList = handlerBatchQuery(param)
        return PageResult(menuList, menuList.count())
    }


    /**
     * 查询xmSelect组件所需要的菜单树
     *
     * @param param SysMenuQueryParam
     * @return XmSelectResult
     */
    @PreCheckPermission(value = ["{}:view"])
    @ApiOperation(value = "xmSelect组件查询菜单树")
    @ResponseBody
    @GetMapping("/selectTree")
    fun xmSelectMenuTree(param: SysMenuQueryParam): XmSelectResult {
        // 条件查询菜单
        val menuList = handlerBatchQuery(param)

        // 菜单转XmSelect对象
        val result = mutableListOf<XmSelect>()
        menuList.forEach {
            result.add(BeanUtil.toBean(it, XmSelect::class.java))
        }
        return XmSelectResult(TreeUtil.buildTree(result, false))
    }

}
