package com.zeta.system.controller

import cn.dev33.satoken.stp.StpUtil
import cn.hutool.core.bean.BeanUtil
import cn.hutool.core.collection.CollUtil
import cn.hutool.core.lang.Assert
import com.baomidou.mybatisplus.core.metadata.IPage
import com.baomidou.mybatisplus.extension.kotlin.KtUpdateWrapper
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport
import com.zeta.system.model.dto.sysRole.SysRoleDTO
import com.zeta.system.model.dto.sysUser.LoginUserDTO
import com.zeta.system.model.dto.sysUser.SysUserSaveDTO
import com.zeta.system.model.dto.sysUser.SysUserUpdateDTO
import com.zeta.system.model.entity.SysMenu
import com.zeta.system.model.entity.SysUser
import com.zeta.system.model.enumeration.MenuTypeEnum
import com.zeta.system.model.enumeration.UserStateEnum
import com.zeta.system.model.param.ChangePasswordParam
import com.zeta.system.model.param.ResetPasswordParam
import com.zeta.system.model.param.SysUserQueryParam
import com.zeta.system.service.ISysRoleMenuService
import com.zeta.system.service.ISysRoleService
import com.zeta.system.service.ISysUserService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.zetaframework.base.controller.SuperAllViewController
import org.zetaframework.base.controller.crud.goView
import org.zetaframework.base.controller.extra.ExistenceController
import org.zetaframework.base.controller.extra.UpdateStateController
import org.zetaframework.base.param.ExistParam
import org.zetaframework.base.param.UpdateStateParam
import org.zetaframework.base.result.ApiResult
import org.zetaframework.core.exception.BusinessException
import org.zetaframework.core.log.enums.LoginStateEnum
import org.zetaframework.core.log.event.SysLoginEvent
import org.zetaframework.core.log.model.SysLoginLogDTO
import org.zetaframework.core.saToken.annotation.PreAuth
import org.zetaframework.core.utils.ContextUtil
import org.zetaframework.core.utils.JSONUtil
import org.zetaframework.core.utils.TreeUtil
import springfox.documentation.annotations.ApiIgnore
import javax.servlet.http.HttpServletRequest

/**
 * 用户 前端控制器
 *
 * @author AutoGenerator
 * @date 2021-12-30 15:24:03
 */
@Api(tags = ["用户管理"])
@PreAuth(replace = "sys:user")
@Controller
@RequestMapping("/system/user")
class SysUserController(
    private val roleService: ISysRoleService,
    private val roleMenuService: ISysRoleMenuService,
    private val applicationContext: ApplicationContext
) : SuperAllViewController<ISysUserService, Long, SysUser, SysUserQueryParam, SysUserSaveDTO, SysUserUpdateDTO>(),
    UpdateStateController<SysUser, Long, Int>,
    ExistenceController<SysUser, Long>
{

    /**
     * 当前模块路径
     *
     * 说明：
     * 1.新增、修改、列表页面会拼接这个路径
     * 2.由具体的业务Controller实现
     *
     * 例如：
     * modulePath: system/user
     * indexPath: system/user/index.html
     * updatePath: system/user/edit.html
     * addPath: system/user/add.html
     */
    override fun getModulePath(): String {
        return "/system/user"
    }

    /**
     * 进入新增页面前的一些处理
     *
     * 说明：
     * 你可以在这里向新增页面传参
     *
     * @param model [Model]
     * @param entity 实体类 路由后面携带的参数会被封装到实体类里面。当然得是实体类中的字段才行
     * @param request 请求对象
     */
    override fun beforeSaveView(model: Model, entity: SysUser?, request: HttpServletRequest) {
        // 获取角色列表
        model.addAttribute("roleList", roleService.list())
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
    override fun beforeUpdateView(model: Model, entity: SysUser?, request: HttpServletRequest) {
        // 获取用户信息
        val user: SysUser = entity?.id?.let {
            service.getById(it)
        } ?: throw BusinessException("用户不存在")
        model.addAttribute("user", user)

        // 获取角色列表
        model.addAttribute("roleList", roleService.list())

        // 获取用户拥有的角色id
        val userRoleIds = service.getUserRoles(user.id!!).map { it.id }
        model.addAttribute("userRoleIds", userRoleIds)
    }

    /**
     * 个人资料页面
     *
     * @param model Model
     */
    @ApiIgnore
    @GetMapping("/info")
    fun infoView(model: Model): String {
        val user = service.getById(ContextUtil.getUserId()) ?: throw BusinessException("用户不存在")
        handlerGetData(user)
        model.addAttribute("user", user)

        // 获取用户角色
        val userRoles: List<SysRoleDTO> = service.getUserRoles(user.id!!)
        model.addAttribute("userRoles", JSONUtil.toJsonStr(userRoles))
        return goView("info.html")
    }

    /**
     * 头像上传页面
     *
     * @param model Model
     */
    @ApiIgnore
    @GetMapping("/profile")
    fun profileView(model: Model): String {
        return goView("profile.html")
    }

    /**
     * 修改密码页面
     *
     * @param model Model
     */
    @ApiIgnore
    @GetMapping("/changePwd")
    fun changePwdView(model: Model): String {
        return goView("changePwd.html")
    }

    /**
     * 处理查询后的数据
     *
     * 说明：
     * 分页查询用户，需要返回用户的角色列表。
     * 所以需要在分页查询完用户之后再查询每个用户的角色
     *
     * @param page IPage<Entity>
     */
    override fun handlerResult(page: IPage<SysUser>) {
        val userList: List<SysUser> = page.records
        val userIds: List<Long> = userList.map { it.id!! }

        if(CollUtil.isNotEmpty(userIds)) {
            // 批量获取用户角色 Map<用户id, 用户角色列表>
            val userRoleMap: Map<Long, List<SysRoleDTO>> = service.getUserRoles(userIds)
            page.records.forEach { user ->
                user.roles = userRoleMap.getOrDefault(user.id, mutableListOf())
            }
        }
    }

    /**
     * 处理单体查询数据
     *
     * @param entity Entity
     */
    override fun handlerGetData(entity: SysUser?) {
        entity?.id?.let { userId ->
            entity.roles = service.getUserRoles(userId)
        }
    }

    /**
     * 自定义新增
     *
     * @param saveDTO SaveDTO 保存对象
     * @return ApiResult<Entity>
     */
    override fun handlerSave(saveDTO: SysUserSaveDTO): ApiResult<Boolean> {
        // 判断是否存在
        if(ExistParam<SysUser, Long>("account", saveDTO.account).isExist(service)) {
            return fail("账号已存在")
        }
        return success(service.saveUser(saveDTO))
    }

    /**
     * 自定义修改
     *
     * @param updateDTO UpdateDTO 修改对象
     * @return ApiResult<Entity>
     */
    override fun handlerUpdate(updateDTO: SysUserUpdateDTO): ApiResult<Boolean> {
        return success(service.updateUser(updateDTO))
    }

    /**
     * 自定义修改状态
     *
     * @param param UpdateStateParam<Id, State> 修改对象
     * @return ApiResult<Boolean>
     */
    override fun handlerUpdateState(param: UpdateStateParam<Long, Int>): ApiResult<Boolean> {
        // 判断状态值是否正常
        Assert.notNull(param.id, "用户id不能为空")
        Assert.notNull(param.state, "状态不能为空")

        // 判断状态参数是否在定义的用户状态中
        if(!UserStateEnum.getAllCode().contains(param.state)) {
            return fail("参数异常")
        }

        // 判断用户是否允许修改
        val user = service.getById(param.id) ?: return fail("用户不存在")
        if(user.readonly != null && user.readonly == true) {
            throw BusinessException("用户[${user.username}]禁止修改状态")
        }

        return super.handlerUpdateState(param)
    }

    /**
     * 自定义单体删除
     *
     * @param id Id
     * @return R<Boolean>
     */
    override fun handlerDelete(id: Long): ApiResult<Boolean> {
        val user = service.getById(id) ?: return success(true)
        // 判断用户是否允许删除
        if(user.readonly != null && user.readonly == true) {
            throw BusinessException("用户[${user.username}]禁止删除")
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
        val userList = service.listByIds(ids) ?: return success(true)
        // 判断是否存在不允许删除的用户
        userList.forEach { user ->
            if(user.readonly != null && user.readonly == true) {
                throw BusinessException("用户[${user.username}]禁止删除")
            }
        }
        return super.handlerBatchDelete(ids)
    }


    /**
     * 修改自己的密码
     *
     * @param param ChangePasswordParam 修改密码的参数
     * @return ApiResult<Boolean>
     */
    @ApiOperation("修改自己的密码")
    @ResponseBody
    @PutMapping("/changePwd")
    fun changePwd(@RequestBody @Validated param: ChangePasswordParam, request: HttpServletRequest): ApiResult<Boolean> {
        val user = service.getById(ContextUtil.getUserId()) ?: throw BusinessException("用户不存在")

        // 旧密码是否正确
        if (!service.comparePassword(param.oldPwd!!, user.password!!)) {
            return fail("旧密码不正确", false)
        }

        // 修改成新密码
        user.password = service.encodePassword(param.newPwd!!)
        if (!service.updateById(user)) {
            return fail("修改失败", false)
        }

        // 登出日志
        applicationContext.publishEvent(SysLoginEvent(SysLoginLogDTO.loginFail(
            user.account ?: "", LoginStateEnum.LOGOUT, "修改密码", request
        )))

        // 下线
        StpUtil.logout(user.id)
        return success("修改成功", true)
    }

    /**
     * 重置密码
     *
     * @param param ResetPasswordParam 重置密码参数
     * @return ApiResult<Boolean>
     */
    @ApiOperation("重置密码")
    @ResponseBody
    @PutMapping("/restPwd")
    fun updatePwd(@RequestBody @Validated param: ResetPasswordParam, request: HttpServletRequest): ApiResult<Boolean> {
        val user = service.getById(param.id) ?: return success(true)
        // 判断用户是否允许重置密码
        if(user.readonly != null && user.readonly == true) {
            throw BusinessException("用户[${user.username}]禁止重置密码")
        }

        // 密码加密， 因为密码已经判空过了所以这里直接param.password!!
        param.password = service.encodePassword(param.password!!)
        val entity = BeanUtil.toBean(param, getEntityClass())

        // 修改密码
        val result = service.updateById(entity)
        if(result) {
            // 登出日志
            applicationContext.publishEvent(SysLoginEvent(SysLoginLogDTO.loginFail(
                user.account ?: "", LoginStateEnum.LOGOUT, "重置密码", request
            )))

            // 让被修改密码的人下线
            StpUtil.logout(entity.id)
        }
        return success(result)
    }


    /**
     * 修改头像
     *
     * @param avatar
     */
    @ApiOperation("修改头像")
    @ResponseBody
    @PutMapping("/updateAvatar")
    fun updateAvatar(avatar: String): ApiResult<Boolean> {
        if (!service.update(KtUpdateWrapper(SysUser())
            .eq(SysUser::id, ContextUtil.getUserId())
            .set(SysUser::avatar, avatar)
        )){
            return fail("修改失败")
        }

        // 修改session中的用户缓存
        val loginUser = StpUtil.getSession()["user"] as LoginUserDTO?
        loginUser?.let {
            it.avatar = avatar
            StpUtil.getSession()["user"] = it
        }
        return success("修改成功", true)
    }


    /**
     * 获取用户菜单
     */
    @ApiOperation("获取用户菜单")
    @ResponseBody
    @GetMapping("/menu")
    fun userMenu(): List<SysMenu> {
        // 查询用户对应的菜单
        val menuList = roleMenuService.listMenuByUserId(
            ContextUtil.getUserId(),
            mutableListOf(MenuTypeEnum.DIRECTORY.code, MenuTypeEnum.MENU.code)
        )

        return TreeUtil.buildTree(menuList, false)
    }
}

