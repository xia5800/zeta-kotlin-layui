package com.zeta.system.controller

import cn.hutool.core.bean.BeanUtil
import cn.hutool.core.collection.CollUtil
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport
import com.zeta.system.model.dto.sysRoleMenu.SysRoleMenuHandleDTO
import com.zeta.system.model.entity.SysMenu
import com.zeta.system.model.entity.SysRoleMenu
import com.zeta.system.model.enumeration.MenuTypeEnum
import com.zeta.system.service.ISysMenuService
import com.zeta.system.service.ISysRoleMenuService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.springframework.stereotype.Controller
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.zetaframework.base.controller.SuperSimpleController
import org.zetaframework.base.controller.view.DefaultView
import org.zetaframework.base.result.ApiResult
import org.zetaframework.base.result.DTree
import org.zetaframework.base.result.DTreeResult
import org.zetaframework.core.saToken.annotation.PreCheckPermission
import org.zetaframework.core.utils.TreeUtil

/**
 * 角色菜单 前端控制器
 *
 * @author AutoGenerator
 * @date 2021-12-30 15:24:03
 */
@Api(tags = ["角色菜单"])
@Controller
@RequestMapping("/system/roleMenu")
class SysRoleMenuController(
    private val menuService: ISysMenuService
) : SuperSimpleController<ISysRoleMenuService, SysRoleMenu>(),
    DefaultView
{

    /**
     * 查询角色菜单树
     *
     * 说明：
     * 用于前端角色管理查询角色对应的菜单树。
     * @param roleId Long   角色id
     * @return [DTreeResult]
     */
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "查询角色菜单")
    @ResponseBody
    @GetMapping("/{roleId}")
    fun list(@PathVariable("roleId") @ApiParam("角色id") roleId: Long): DTreeResult {
        val dTrees = mutableListOf<DTree>()

        // 查询所有菜单
        val menuList = menuService.list(KtQueryWrapper(SysMenu()).orderByAsc(SysMenu::sortValue)) ?: return DTreeResult(dTrees)

        // 查询该角色可以访问的菜单
        val roleMenuList = service.list(KtQueryWrapper(SysRoleMenu()).eq(SysRoleMenu::roleId, roleId))
        for (menu in menuList) {
            // 判断角色是否能访问该菜单
            val checked = roleMenuList.any {
                it.menuId == menu.id
            }

            // 构造Dtree对象，并判断该对象复选框是否选中。
            // ps: 因为menu是TreeEntity对象，DTree也是TreeEntity对象。Dtree组件所需的id、parentId、title都有，所以可以直接BeanCopy
            val dTree = BeanUtil.toBean(menu, DTree::class.java)
            dTree.checkArr = mutableListOf(DTree.CheckArr(checked =  if (checked) "1" else "0"))
            dTree.iconClass = menu.icon
            dTree.last = menu.type == MenuTypeEnum.RESOURCE.code
            dTrees.add(dTree)
        }
        return DTreeResult(dTrees)
    }


    /**
     * 新增或修改
     *
     * @param roleMenuHandleDto SysRoleMenuHandleDTO 批量新增、修改角色菜单关联关系参数
     * @return ApiResult<Boolean>
     */
    @PreCheckPermission(value = ["sys:role:save", "sys:role:update"]) // 同时有新增、修改角色权限
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "新增或修改")
    @ResponseBody
    @PutMapping
    fun update(@RequestBody @Validated roleMenuHandleDto: SysRoleMenuHandleDTO): ApiResult<Boolean> {
        // 修改前先删除角色所有权限
        service.remove(KtQueryWrapper(SysRoleMenu()).eq(SysRoleMenu::roleId, roleMenuHandleDto.roleId))
        if (CollUtil.isNotEmpty(roleMenuHandleDto.menuIds)) {
            val batchList = mutableListOf<SysRoleMenu>()
            roleMenuHandleDto.menuIds!!.forEach {
                batchList.add(SysRoleMenu(roleMenuHandleDto.roleId, it))
            }
            if (!service.saveBatch(batchList)) return fail("操作失败")
            // 删除用户角色、权限缓存
            service.clearUserCache(roleMenuHandleDto.roleId!!)
        }
        return success(true)
    }


}
