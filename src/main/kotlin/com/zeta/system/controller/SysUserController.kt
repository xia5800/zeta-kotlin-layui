package com.zeta.system.controller

import cn.afterturn.easypoi.excel.entity.ImportParams
import cn.dev33.satoken.stp.StpUtil
import cn.hutool.core.bean.BeanUtil
import cn.hutool.core.lang.Assert
import cn.hutool.core.util.StrUtil
import com.baomidou.mybatisplus.extension.kotlin.KtUpdateWrapper
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport
import com.zeta.system.model.dto.sysRole.SysRoleDTO
import com.zeta.system.model.dto.sysUser.LoginUserDTO
import com.zeta.system.model.dto.sysUser.SysUserDTO
import com.zeta.system.model.dto.sysUser.SysUserSaveDTO
import com.zeta.system.model.dto.sysUser.SysUserUpdateDTO
import com.zeta.system.model.entity.SysMenu
import com.zeta.system.model.entity.SysRole
import com.zeta.system.model.entity.SysUser
import com.zeta.system.model.enums.MenuTypeEnum
import com.zeta.system.model.enums.UserStateEnum
import com.zeta.system.model.param.ChangePasswordParam
import com.zeta.system.model.param.ResetPasswordParam
import com.zeta.system.model.param.SysUserQueryParam
import com.zeta.system.model.poi.SysUserExportPoi
import com.zeta.system.model.poi.SysUserImportPoi
import com.zeta.system.poi.SysUserExcelVerifyHandler
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
import org.zetaframework.base.controller.SuperNoQueryController
import org.zetaframework.base.controller.extra.ExistenceController
import org.zetaframework.base.controller.extra.NoPageQueryController
import org.zetaframework.base.controller.extra.PoiController
import org.zetaframework.base.controller.extra.UpdateStateController
import org.zetaframework.base.controller.goView
import org.zetaframework.base.controller.view.AllView
import org.zetaframework.base.param.ExistParam
import org.zetaframework.base.param.PageParam
import org.zetaframework.base.param.UpdateStateParam
import org.zetaframework.base.result.ApiResult
import org.zetaframework.base.result.PageResult
import org.zetaframework.core.exception.ArgumentException
import org.zetaframework.core.exception.BusinessException
import org.zetaframework.core.log.annotation.SysLog
import org.zetaframework.core.log.enums.LoginStateEnum
import org.zetaframework.core.log.event.LoginEvent
import org.zetaframework.core.log.model.LoginLogDTO
import org.zetaframework.core.saToken.annotation.PreAuth
import org.zetaframework.core.saToken.annotation.PreCheckPermission
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
    private val applicationContext: ApplicationContext,
    private val sysUserExcelVerifyHandler: SysUserExcelVerifyHandler
) : SuperNoQueryController<ISysUserService, Long, SysUser, SysUserSaveDTO, SysUserUpdateDTO>(),
    NoPageQueryController<SysUser, Long, SysUserQueryParam>,
    UpdateStateController<SysUser, Long, Int>,
    ExistenceController<SysUser, Long>,
    PoiController<SysUserImportPoi, SysUserExportPoi, SysUser, SysUserQueryParam>,
    AllView<SysUser>
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
    fun profileView(model: Model): String = goView("profile.html")

    /**
     * 修改密码页面
     *
     * @param model Model
     */
    @ApiIgnore
    @GetMapping("/changePwd")
    fun changePwdView(model: Model): String = goView("changePwd.html")


    /**
     * 分页查询
     *
     * @param param 分页参数
     * @param queryParam 分页查询条件
     * @return ApiResult<PageResult<Entity>>
     */
    @PreCheckPermission(value = ["{}:view"])
    @ApiOperationSupport(order = 10)
    @ApiOperation(value = "分页查询")
    @SysLog(response = false)
    @ResponseBody
    @GetMapping("/page")
    fun page(pageParam: PageParam, queryParam: SysUserQueryParam?): PageResult<SysUserDTO> {
        // 默认排序
        if (StrUtil.isBlank(pageParam.sort)) {
            pageParam.setDefaultDesc("id")
        }
        return service.customPage(pageParam, queryParam)
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
     * 处理批量查询数据
     * @param list MutableList<Entity>
     */
    override fun handlerBatchData(list: MutableList<SysUser>) {
        if (list.isEmpty()) return
        val userIds: List<Long> = list.map { it.id!! }
        // 批量获取用户角色 Map<用户id, 用户角色列表>
        val userRoleMap: Map<Long, List<SysRoleDTO>> = service.getUserRoles(userIds)
        list.forEach { user ->
            user.roles = userRoleMap.getOrDefault(user.id, mutableListOf())
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
        if (ExistParam<SysUser, Long>("account", saveDTO.account).isExist(service)) {
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
        if (!UserStateEnum.getAllCode().contains(param.state)) {
            return fail("参数异常")
        }

        // 判断用户是否允许修改
        val user = service.getById(param.id) ?: return fail("用户不存在")
        if (user.readonly != null && user.readonly == true) {
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
        if (user.readonly != null && user.readonly == true) {
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
            if (user.readonly != null && user.readonly == true) {
                throw BusinessException("用户[${user.username}]禁止删除")
            }
        }
        return super.handlerBatchDelete(ids)
    }

    /**
     * 导入参数增强
     *
     * 说明：
     * 你可以在这里对ImportParams配置进行一些补充
     * 例如设置excel验证规则、校验组、校验处理接口等
     */
    override fun enhanceImportParams(importParams: ImportParams) {
        // 为了保证[SysUserImportPoi]中的校验注解生效。将Excel校验开启
        importParams.isNeedVerify = true
        // 校验处理接口：用户名重复校验
        importParams.verifyHandler = sysUserExcelVerifyHandler
        // 也可以这样写 (写一个匿名接口)
        // importParams.verifyHandler = object: IExcelVerifyHandler<SysUserImportPoi> {
        //     override fun verifyHandler(obj: SysUserImportPoi): ExcelVerifyHandlerResult {
        //         // 判断是否存在
        //         return if (ExistParam<SysUser, Long>(SysUser::account, obj.account).isExist(service)) {
        //             ExcelVerifyHandlerResult(false, "账号已存在")
        //         } else ExcelVerifyHandlerResult(true, "")
        //     }
        // }
    }

    /**
     * 处理导入数据
     *
     * 说明：
     * 1.你需要手动实现导入逻辑
     * 2.enhanceImportParams方法里，配置了导入参数校验、用户名是否重复校验。所以这里的list里面的数据是不需要校验的
     */
    override fun handlerImport(list: MutableList<SysUserImportPoi>): ApiResult<Boolean> {
        val batchList: List<SysUser> = list.map { importPoi ->
            val user = BeanUtil.toBean(importPoi, SysUser::class.java)
            // 处理密码
            user.password = service.encodePassword(importPoi.password!!)
            // 处理角色
            if (!importPoi.roleNames.isNullOrBlank()) {
                val roles: List<SysRole> = roleService.getRolesByNames(importPoi.roleNames!!.split(StrUtil.COMMA))
                user.roles = roles.map { BeanUtil.toBean(it, SysRoleDTO::class.java) }
            }
            // 其它处理
            user.readonly = false
            user.state = UserStateEnum.NORMAL.code
            user
        }
        return success(service.batchImportUser(batchList))
    }

    /**
     * 获取待导出的数据
     *
     * @param param QueryParam
     * @return MutableList<ExportBean>
     */
    override fun findExportList(param: SysUserQueryParam): MutableList<SysUserExportPoi> {
        // 判断状态参数是否在定义的用户状态中
        if (param.state != null && !UserStateEnum.getAllCode().contains(param.state!!)) {
            throw ArgumentException("状态参数异常")
        }

        // 条件查询Entity数据
        val list = super.handlerBatchQuery(param)
        if (list.isEmpty()) return mutableListOf()

        // Entity -> ExportBean
        return list.map { user ->
            val exportPoi = BeanUtil.toBean(user, SysUserExportPoi::class.java)
            // 处理用户角色 ps:导出角色名还是导出角色编码看需求
            exportPoi.roles = user.roles?.mapNotNull { it.name }
            exportPoi
        }.toMutableList()
    }

    /**
     * 重置密码
     *
     * @param param ResetPasswordParam 重置密码参数
     * @return ApiResult<Boolean>
     */
    @PreCheckPermission(value = ["{}:update"])
    @ApiOperationSupport(order = 100)
    @ApiOperation(value = "重置密码")
    @SysLog(request = false)
    @ResponseBody
    @PutMapping("/restPwd")
    fun updatePwd(@RequestBody @Validated param: ResetPasswordParam, request: HttpServletRequest): ApiResult<Boolean> {
        val user = service.getById(param.id) ?: return success(true)
        // 判断用户是否允许重置密码
        if (user.readonly != null && user.readonly == true) {
            throw BusinessException("用户[${user.username}]禁止重置密码")
        }

        // 密码加密， 因为密码已经判空过了所以这里直接param.password!!
        user.password = service.encodePassword(param.password!!)

        // 修改密码
        if (!service.updateById(user)) {
            return fail("重置密码失败")
        }

        // 登出日志
        applicationContext.publishEvent(
            LoginEvent(
                LoginLogDTO.loginFail(
                    user.account ?: "", LoginStateEnum.LOGOUT, "重置密码", request
                )
            )
        )

        // 让被修改密码的人下线
        StpUtil.logout(user.id)
        return success(true)
    }

    /**
     * 修改自己的密码
     *
     * @param param ChangePasswordParam 修改密码的参数
     * @return ApiResult<Boolean>
     */
    @ApiOperationSupport(order = 101)
    @ApiOperation(value = "修改自己的密码")
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
        applicationContext.publishEvent(
            LoginEvent(
                LoginLogDTO.loginFail(
                    user.account ?: "", LoginStateEnum.LOGOUT, "修改密码", request
                )
            )
        )

        // 下线
        StpUtil.logout(user.id)
        return success("修改成功", true)
    }

    /**
     * 修改自己的头像
     *
     * @param avatar
     */
    @ApiOperationSupport(order = 110)
    @ApiOperation(value = "修改自己的头像")
    @SysLog
    @ResponseBody
    @PutMapping("/updateAvatar")
    fun updateAvatar(avatar: String): ApiResult<Boolean> {
        if (!service.update(
                KtUpdateWrapper(SysUser())
                    .eq(SysUser::id, ContextUtil.getUserId())
                    .set(SysUser::avatar, avatar)
            )
        ) {
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
     * 获取当前用户菜单
     */
    @ApiOperationSupport(order = 120)
    @ApiOperation(value = "获取当前用户菜单")
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

