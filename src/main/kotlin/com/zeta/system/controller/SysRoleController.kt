package com.zeta.system.controller

import cn.hutool.core.lang.Assert
import com.zeta.system.model.dto.sysRole.SysRoleSaveDTO
import com.zeta.system.model.dto.sysRole.SysRoleUpdateDTO
import com.zeta.system.model.entity.SysRole
import com.zeta.system.model.param.SysRoleQueryParam
import com.zeta.system.service.ISysRoleService
import io.swagger.annotations.Api
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.zetaframework.base.controller.SuperAllViewController
import org.zetaframework.base.controller.crud.goView
import org.zetaframework.base.controller.extra.ExistenceController
import org.zetaframework.base.param.ExistParam
import org.zetaframework.base.result.ApiResult
import org.zetaframework.core.exception.ArgumentException
import org.zetaframework.core.exception.BusinessException
import org.zetaframework.core.saToken.annotation.PreAuth
import springfox.documentation.annotations.ApiIgnore
import javax.servlet.http.HttpServletRequest

/**
 * 角色 前端控制器
 *
 * @author AutoGenerator
 * @date 2021-12-30 15:24:03
 */
@Api(tags = ["角色管理"])
@PreAuth(replace = "sys:role")
@Controller
@RequestMapping("/system/role")
class SysRoleController:
    SuperAllViewController<ISysRoleService, Long, SysRole, SysRoleQueryParam, SysRoleSaveDTO, SysRoleUpdateDTO>(),
    ExistenceController<SysRole, Long>
{

    /**
     * 当前模块路径
     */
    override fun getModulePath(): String {
        return "/system/role"
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
    override fun beforeUpdateView(model: Model, entity: SysRole?, request: HttpServletRequest) {
        // 获取用户信息
        val role: SysRole = entity?.id?.let {
            service.getById(it)
        } ?: throw BusinessException("角色不存在")
        model.addAttribute("role", role)
    }

    /**
     * 前往分配权限页面
     *
     * @param model [Model]
     * @param entity 实体类 路由后面携带的参数会被封装到实体类里面。当然得是实体类中的字段才行
     * @param request 请求对象
     */
    @ApiIgnore
    @GetMapping("/auth")
    fun authView(model: Model, entity: SysRole?, request: HttpServletRequest): String {
        // 获取角色id
        Assert.notNull(entity?.id, "角色id不能为空")
        model.addAttribute("roleId", entity?.id)

        return goView("auth.html")
    }

    /**
     * 自定义新增
     *
     * @param saveDTO SaveDTO 保存对象
     * @return ApiResult<Entity>
     */
    override fun handlerSave(saveDTO: SysRoleSaveDTO): ApiResult<Boolean> {
        // 判断是否存在
        if(ExistParam<SysRole, Long>("code", saveDTO.code).isExist(service)) {
            return fail("角色编码已存在")
        }

        return super.handlerSave(saveDTO)
    }


    /**
     * 自定义修改
     *
     * @param updateDTO UpdateDTO 修改对象
     * @return ApiResult<Entity>
     */
    override fun handlerUpdate(updateDTO: SysRoleUpdateDTO): ApiResult<Boolean> {
        // 判断是否存在
        if(ExistParam<SysRole, Long>("code", updateDTO.code, updateDTO.id).isExist(service)) {
            return fail("角色编码已存在")
        }

        // 判断角色是否允许修改
        val role = service.getById(updateDTO.id) ?: return fail("角色不存在")
        if(role.readonly != null && role.readonly == true) {
            throw BusinessException("角色[${role.name}]禁止修改")
        }

        return super.handlerUpdate(updateDTO)
    }

    /**
     * 自定义单体删除
     *
     * @param id Id
     * @return R<Boolean>
     */
    override fun handlerDelete(id: Long): ApiResult<Boolean> {
        val role = service.getById(id) ?: return success(true)
        // 判断角色是否允许删除
        if(role.readonly != null && role.readonly == true) {
            throw BusinessException("角色[${role.name}]禁止删除")
        }

        return super.handlerDelete(id)
    }

    /**
     * 自定义批量删除
     *
     * @param ids Id
     * @return R<Boolean>
     */
    override fun handlerBatchDelete(ids: MutableList<Long>): ApiResult<Boolean> {
        val roleList = service.listByIds(ids) ?: return success(true)
        // 判断是否存在不允许删除的角色
        roleList.forEach { role ->
            if(role.readonly != null && role.readonly == true) {
                throw BusinessException("角色[${role.name}]禁止删除")
            }
        }

        return super.handlerBatchDelete(ids)
    }
}
