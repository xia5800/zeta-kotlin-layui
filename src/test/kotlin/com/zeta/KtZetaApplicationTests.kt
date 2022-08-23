package com.zeta

import cn.dev33.satoken.secure.BCrypt
import com.zeta.system.model.entity.*
import com.zeta.system.model.enumeration.MenuTypeEnum
import com.zeta.system.model.enumeration.SexEnum
import com.zeta.system.model.enumeration.UserStateEnum
import com.zeta.system.service.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.zetaframework.core.mybatisplus.generator.UidGenerator

/**
 *
 * @author gcc
 */
@SpringBootTest
class KtZetaApplicationTests {

    @Autowired
    private lateinit var uidGenerator: UidGenerator
    @Autowired
    private lateinit var menuService: ISysMenuService
    @Autowired
    private lateinit var roleService: ISysRoleService
    @Autowired
    private lateinit var userService: ISysUserService
    @Autowired
    private lateinit var userRoleService: ISysUserRoleService
    @Autowired
    private lateinit var roleMenuService: ISysRoleMenuService
    @Autowired
    private lateinit var sysDictService: ISysDictService
    @Autowired
    private lateinit var sysDictItemService: ISysDictItemService

    /**
     * 初始化数据库
     */
    // @Test // 注释掉，防止maven打包的时候没有跳过测试
    fun initDatabase() {
        // 初始化系统菜单、权限
        initMenu()
        // 初始化角色
        val superAdminId = initRole()
        // 初始化超级管理员菜单权限
        initRoleMenu(superAdminId)
        // 初始化超级管理员用户
        val userId = initAdminUser()
        // 初始化用户角色
        initUserRole(userId, superAdminId)
        // 初始化数据字典
        initSysDict()
    }


    /**
     * 初始化系统菜单、权限
     *
     * 说明：
     * 1.确定了前端，使用[Pear Admin Layui](https://gitee.com/pear-admin/Pear-Admin-Layui) 感谢作者：就眠儀式
     * 2.菜单[图标地址](http://layui-doc.pearadmin.com/doc/element/icon.html)
     */
    fun initMenu() {
        val batchList: MutableList<SysMenu> = mutableListOf()
        var menuSort = 1

        // dashboard
        var dashboardSort = 1
        val dashboardId = uidGenerator.getUid()
        batchList.add(buildMenu(dashboardId, 0L, menuSort++, "dashboard", MenuTypeEnum.DIRECTORY, icon = "layui-icon layui-icon-console"))
        // dashboard-分析页
        val dashboardAnalysisId = uidGenerator.getUid()
        batchList.add(buildMenu(dashboardAnalysisId, dashboardId, dashboardSort++, "分析页", MenuTypeEnum.MENU, path = "/dashboard"))
        // dashboard-工作台
        val dashboardWorkbenchId = uidGenerator.getUid()
        batchList.add(buildMenu(dashboardWorkbenchId, dashboardId, dashboardSort++, "工作台", MenuTypeEnum.MENU, path = "/dashboard/workbench"))

        // 系统管理
        var systemSort = 1
        val systemId = uidGenerator.getUid()
        batchList.add(buildMenu(systemId, 0L, menuSort++, "系统管理", MenuTypeEnum.DIRECTORY, icon = "layui-icon layui-icon-set-fill"))
        // 系统管理-用户管理
        val userId = uidGenerator.getUid()
        val userIdR = uidGenerator.getUid()
        val userIdC = uidGenerator.getUid()
        val userIdU = uidGenerator.getUid()
        val userIdD = uidGenerator.getUid()
        batchList.add(buildMenu(userId, systemId, systemSort++, "用户管理", MenuTypeEnum.MENU, path = "/system/user/index"))
        batchList.add(buildButton(userIdR, userId, 1, "查看用户", "sys:user:view"))
        batchList.add(buildButton(userIdC, userId, 2, "新增用户", "sys:user:save"))
        batchList.add(buildButton(userIdU, userId, 3, "修改用户", "sys:user:update"))
        batchList.add(buildButton(userIdD, userId, 4, "删除用户", "sys:user:delete"))
        // 系统管理-角色管理
        val roleId = uidGenerator.getUid()
        val roleIdR = uidGenerator.getUid()
        val roleIdC = uidGenerator.getUid()
        val roleIdU = uidGenerator.getUid()
        val roleIdD = uidGenerator.getUid()
        batchList.add(buildMenu(roleId, systemId, systemSort++, "角色管理", MenuTypeEnum.MENU, path = "/system/role/index"))
        batchList.add(buildButton(roleIdR, roleId, 1, "查看角色", "sys:role:view"))
        batchList.add(buildButton(roleIdC, roleId, 2, "新增角色", "sys:role:save"))
        batchList.add(buildButton(roleIdU, roleId, 3, "修改角色", "sys:role:update"))
        batchList.add(buildButton(roleIdD, roleId, 4, "删除角色", "sys:role:delete"))
        // 系统管理-菜单管理
        val menuId = uidGenerator.getUid()
        val menuIdR = uidGenerator.getUid()
        val menuIdC = uidGenerator.getUid()
        val menuIdU = uidGenerator.getUid()
        val menuIdD = uidGenerator.getUid()
        batchList.add(buildMenu(menuId, systemId, systemSort++, "菜单管理", MenuTypeEnum.MENU, path = "/system/menu/index"))
        batchList.add(buildButton(menuIdR, menuId, 1, "查看菜单", "sys:menu:view"))
        batchList.add(buildButton(menuIdC, menuId, 2, "新增菜单", "sys:menu:save"))
        batchList.add(buildButton(menuIdU, menuId, 3, "修改菜单", "sys:menu:update"))
        batchList.add(buildButton(menuIdD, menuId, 4, "删除菜单", "sys:menu:delete"))
        // 系统管理-操作日志
        val optId = uidGenerator.getUid()
        val optIdR = uidGenerator.getUid()
        batchList.add(buildMenu(optId, systemId, systemSort++, "操作日志", MenuTypeEnum.MENU, path = "/system/optLog/index"))
        batchList.add(buildButton(optIdR, optId, 1, "查看操作日志", "sys:optLog:view"))
        // 系统管理-登录日志
        val loginLogId = uidGenerator.getUid()
        val loginLogIdR = uidGenerator.getUid()
        batchList.add(buildMenu(loginLogId, systemId, systemSort++, "登录日志", MenuTypeEnum.MENU, path = "/system/loginLog/index"))
        batchList.add(buildButton(loginLogIdR, loginLogId, 1, "查看登录日志", "sys:loginLog:view"))
        // 系统管理-文件管理
        val fileId = uidGenerator.getUid()
        val fileIdR = uidGenerator.getUid()
        val fileIdC = uidGenerator.getUid()
        val fileIdE = uidGenerator.getUid()
        val fileIdD = uidGenerator.getUid()
        batchList.add(buildMenu(fileId, systemId, systemSort++, "文件管理", MenuTypeEnum.MENU, path = "/system/file/index"))
        batchList.add(buildButton(fileIdR, fileId, 1, "查看文件", "sys:file:view"))
        batchList.add(buildButton(fileIdC, fileId, 2, "上传文件", "sys:file:save"))
        batchList.add(buildButton(fileIdE, fileId, 3, "下载文件", "sys:file:export"))
        batchList.add(buildButton(fileIdD, fileId, 4, "删除文件", "sys:file:delete"))
        // 系统管理-数据字典
        val dictId = uidGenerator.getUid()
        val dictIdR = uidGenerator.getUid()
        val dictIdC = uidGenerator.getUid()
        val dictIdU = uidGenerator.getUid()
        val dictIdD = uidGenerator.getUid()
        batchList.add(buildMenu(dictId, systemId, systemSort++, "数据字典", MenuTypeEnum.MENU, path = "/system/dict/index"))
        batchList.add(buildButton(dictIdR, dictId, 1, "查看字典", "sys:dict:view"))
        batchList.add(buildButton(dictIdC, dictId, 2, "新增字典", "sys:dict:save"))
        batchList.add(buildButton(dictIdU, dictId, 3, "修改字典", "sys:dict:update"))
        batchList.add(buildButton(dictIdD, dictId, 4, "删除字典", "sys:dict:delete"))
        val dictItemR = uidGenerator.getUid()
        val dictItemC = uidGenerator.getUid()
        val dictItemU = uidGenerator.getUid()
        val dictItemD = uidGenerator.getUid()
        batchList.add(buildButton(dictItemR, dictId, 5, "查看字典项", "sys:dictItem:view"))
        batchList.add(buildButton(dictItemC, dictId, 6, "新增字典项", "sys:dictItem:save"))
        batchList.add(buildButton(dictItemU, dictId, 7, "修改字典项", "sys:dictItem:update"))
        batchList.add(buildButton(dictItemD, dictId, 8, "删除字典项", "sys:dictItem:delete"))

        // 系统监控
        var monitoringSort = 1
        val monitoringId = uidGenerator.getUid()
        batchList.add(buildMenu(monitoringId, 0L, 999, "系统监控", MenuTypeEnum.DIRECTORY, icon = "layui-icon layui-icon-auz"))
        // 系统监控-druid
        val druidId = uidGenerator.getUid()
        batchList.add(buildMenu(druidId, monitoringId, monitoringSort++, "druid", MenuTypeEnum.MENU, path = "/druid"))
        // 系统监控-swagger
        val swaggerId = uidGenerator.getUid()
        batchList.add(buildMenu(swaggerId, monitoringId, monitoringSort++, "swagger", MenuTypeEnum.MENU, path = "/doc.html"))


        // 案例
        var exampleSort = 1
        val exampleId = uidGenerator.getUid()
        batchList.add(buildMenu(exampleId, 0L, 1000, "案例", MenuTypeEnum.DIRECTORY, icon = "layui-icon layui-icon-template"))
        // 案例-常用组件
        var exampleComponentSort = 1
        val exampleComponentId = uidGenerator.getUid()
        batchList.add(buildMenu(exampleComponentId, exampleId, exampleSort++, "常用组件", MenuTypeEnum.DIRECTORY))
        // 案例-常用组件-基础组件（包含core.html）
        var exampleCommonSort = 1
        val exampleCommonId = uidGenerator.getUid()
        batchList.add(buildMenu(exampleCommonId, exampleComponentId, exampleComponentSort++, "基础组件", MenuTypeEnum.DIRECTORY))
        val exampleCommonCoreId = uidGenerator.getUid()
        batchList.add(buildMenu(exampleCommonCoreId, exampleCommonId, exampleCommonSort++, "酸爽翻倍", MenuTypeEnum.MENU, path = "/example/document/core"))
        val exampleCommonButtonId = uidGenerator.getUid()
        batchList.add(buildMenu(exampleCommonButtonId, exampleCommonId, exampleCommonSort++, "功能按钮", MenuTypeEnum.MENU, path = "/example/document/button"))
        val exampleCommonFormId = uidGenerator.getUid()
        batchList.add(buildMenu(exampleCommonFormId, exampleCommonId, exampleCommonSort++, "表单集合", MenuTypeEnum.MENU, path = "/example/document/form"))
        val exampleCommonIconId = uidGenerator.getUid()
        batchList.add(buildMenu(exampleCommonIconId, exampleCommonId, exampleCommonSort++, "字体图标", MenuTypeEnum.MENU, path = "/example/document/icon"))
        val exampleCommonSelectId = uidGenerator.getUid()
        batchList.add(buildMenu(exampleCommonSelectId, exampleCommonId, exampleCommonSort++, "多选下拉", MenuTypeEnum.MENU, path = "/example/document/select"))
        val exampleCommonTagId = uidGenerator.getUid()
        batchList.add(buildMenu(exampleCommonTagId, exampleCommonId, exampleCommonSort++, "动态标签", MenuTypeEnum.MENU, path = "/example/document/tag"))
        // 案例-常用组件-进阶组件
        var exampleAdvanceSort = 1
        val exampleAdvanceId = uidGenerator.getUid()
        batchList.add(buildMenu(exampleAdvanceId, exampleComponentId, exampleComponentSort++, "进阶组件", MenuTypeEnum.DIRECTORY))
        val exampleCommonTableId = uidGenerator.getUid()
        batchList.add(buildMenu(exampleCommonTableId, exampleAdvanceId, exampleAdvanceSort++, "数据表格", MenuTypeEnum.MENU, path = "/example/document/table"))
        val exampleCommonStepId = uidGenerator.getUid()
        batchList.add(buildMenu(exampleCommonStepId, exampleAdvanceId, exampleAdvanceSort++, "分步表单", MenuTypeEnum.MENU, path = "/example/document/step"))
        val exampleCommonTreeTableId = uidGenerator.getUid()
        batchList.add(buildMenu(exampleCommonTreeTableId, exampleAdvanceId, exampleAdvanceSort++, "树形表格", MenuTypeEnum.MENU, path = "/example/document/treetable"))
        val exampleCommonDTreeId = uidGenerator.getUid()
        batchList.add(buildMenu(exampleCommonDTreeId, exampleAdvanceId, exampleAdvanceSort++, "树状结构", MenuTypeEnum.MENU, path = "/example/document/dtree"))
        val exampleCommonTinymceId = uidGenerator.getUid()
        batchList.add(buildMenu(exampleCommonTinymceId, exampleAdvanceId, exampleAdvanceSort++, "文本编辑", MenuTypeEnum.MENU, path = "/example/document/tinymce"))
        val exampleCommonCardId = uidGenerator.getUid()
        batchList.add(buildMenu(exampleCommonCardId, exampleAdvanceId, exampleAdvanceSort++, "卡片组件", MenuTypeEnum.MENU, path = "/example/document/card"))
        // 案例-常用组件-高级组件
        var exampleHighSort = 1
        val exampleHighId = uidGenerator.getUid()
        batchList.add(buildMenu(exampleHighId, exampleComponentId, exampleComponentSort++, "高级组件", MenuTypeEnum.DIRECTORY))
        val exampleCommonTabId = uidGenerator.getUid()
        batchList.add(buildMenu(exampleCommonTabId, exampleHighId, exampleHighSort++, "多选项卡", MenuTypeEnum.MENU, path = "/example/document/tab"))
        val exampleCommonMenuId = uidGenerator.getUid()
        batchList.add(buildMenu(exampleCommonMenuId, exampleHighId, exampleHighSort++, "数据菜单", MenuTypeEnum.MENU, path = "/example/document/menu"))
        // 案例-常用组件-弹层组件
        var exampleLayerSort = 1
        val exampleLayerId = uidGenerator.getUid()
        batchList.add(buildMenu(exampleLayerId, exampleComponentId, exampleComponentSort++, "弹层组件", MenuTypeEnum.DIRECTORY))
        val exampleCommonDrawerId = uidGenerator.getUid()
        batchList.add(buildMenu(exampleCommonDrawerId, exampleLayerId, exampleLayerSort++, "抽屉组件", MenuTypeEnum.MENU, path = "/example/document/drawer"))
        val exampleCommonNoticeId = uidGenerator.getUid()
        batchList.add(buildMenu(exampleCommonNoticeId, exampleLayerId, exampleLayerSort++, "消息通知 (过时)", MenuTypeEnum.MENU, path = "/example/document/notice"))
        val exampleCommonToastId = uidGenerator.getUid()
        batchList.add(buildMenu(exampleCommonToastId, exampleLayerId, exampleLayerSort++, "消息通知 (新增)", MenuTypeEnum.MENU, path = "/example/document/toast"))
        val exampleCommonLoadingId = uidGenerator.getUid()
        batchList.add(buildMenu(exampleCommonLoadingId, exampleLayerId, exampleLayerSort++, "加载组件", MenuTypeEnum.MENU, path = "/example/document/loading"))
        val exampleCommonPopupId = uidGenerator.getUid()
        batchList.add(buildMenu(exampleCommonPopupId, exampleLayerId, exampleLayerSort++, "弹层组件", MenuTypeEnum.MENU, path = "/example/document/popup"))
        // 案例-常用组件-其他组件
        var exampleOtherSort = 1
        val exampleOtherId = uidGenerator.getUid()
        batchList.add(buildMenu(exampleOtherId, exampleComponentId, exampleComponentSort++, "其他组件", MenuTypeEnum.DIRECTORY))
        val exampleCommonEncryptId = uidGenerator.getUid()
        batchList.add(buildMenu(exampleCommonEncryptId, exampleOtherId, exampleOtherSort++, "哈希加密", MenuTypeEnum.MENU, path = "/example/document/encrypt"))
        val exampleCommonIconPickerId = uidGenerator.getUid()
        batchList.add(buildMenu(exampleCommonIconPickerId, exampleOtherId, exampleOtherSort++, "图标选择", MenuTypeEnum.MENU, path = "/example/document/iconPicker"))
        val exampleCommonAreaId = uidGenerator.getUid()
        batchList.add(buildMenu(exampleCommonAreaId, exampleOtherId, exampleOtherSort++, "省市级联", MenuTypeEnum.MENU, path = "/example/document/area"))
        val exampleCommonCountId = uidGenerator.getUid()
        batchList.add(buildMenu(exampleCommonCountId, exampleOtherId, exampleOtherSort++, "数字滚动", MenuTypeEnum.MENU, path = "/example/document/count"))
        val exampleCommonTopBarId = uidGenerator.getUid()
        batchList.add(buildMenu(exampleCommonTopBarId, exampleOtherId, exampleOtherSort++, "顶部返回", MenuTypeEnum.MENU, path = "/example/document/topBar"))
        val exampleCommonWatermarkId = uidGenerator.getUid()
        batchList.add(buildMenu(exampleCommonWatermarkId, exampleOtherId, exampleOtherSort++, "水印组件", MenuTypeEnum.MENU, path = "/example/document/watermark"))
        val exampleCommonFullscreenId = uidGenerator.getUid()
        batchList.add(buildMenu(exampleCommonFullscreenId, exampleOtherId, exampleOtherSort++, "全屏组件", MenuTypeEnum.MENU, path = "/example/document/fullscreen"))
        val exampleCommonPopoverId = uidGenerator.getUid()
        batchList.add(buildMenu(exampleCommonPopoverId, exampleOtherId, exampleOtherSort++, "汽泡组件", MenuTypeEnum.MENU, path = "/example/document/popover"))
        // 案例-结果页面
        var exampleResultSort = 1
        val exampleResultId = uidGenerator.getUid()
        batchList.add(buildMenu(exampleResultId, exampleId, exampleSort++, "结果页面", MenuTypeEnum.DIRECTORY))
        val exampleResultSuccessId = uidGenerator.getUid()
        batchList.add(buildMenu(exampleResultSuccessId, exampleResultId, exampleResultSort++, "成功", MenuTypeEnum.MENU, path = "/example/result/success"))
        val exampleResultErrorId = uidGenerator.getUid()
        batchList.add(buildMenu(exampleResultErrorId, exampleResultId, exampleResultSort++, "失败", MenuTypeEnum.MENU, path = "/example/result/error"))
        // 案例-数据图表
        var exampleChartSort = 1
        val exampleChartId = uidGenerator.getUid()
        batchList.add(buildMenu(exampleChartId, exampleId, exampleSort++, "数据图表", MenuTypeEnum.DIRECTORY))
        val exampleChartLineId = uidGenerator.getUid()
        batchList.add(buildMenu(exampleChartLineId, exampleChartId, exampleChartSort++, "折线图", MenuTypeEnum.MENU, path = "/example/echarts/line"))
        val exampleChartColumnId = uidGenerator.getUid()
        batchList.add(buildMenu(exampleChartColumnId, exampleChartId, exampleChartSort++, "柱状图", MenuTypeEnum.MENU, path = "/example/echarts/column"))

        menuService.saveBatch(batchList)
    }


    /**
     * 初始化角色
     */
    fun initRole(): Long {
        val batchList: MutableList<SysRole> = mutableListOf()

        val superAdminId = uidGenerator.getUid()
        val adminId = uidGenerator.getUid()
        val userId = uidGenerator.getUid()
        batchList.add(SysRole().apply { id = superAdminId; name = "超级管理员"; code = "SUPER_ADMIN"; describe = "超级管理员，拥有至高无上的权利"; readonly = true  })
        batchList.add(SysRole().apply { id = adminId; name = "管理员"; code = "ADMIN"; describe = "管理员，拥有99%的权利"  })
        batchList.add(SysRole().apply { id = userId; name = "普通用户"; code = "USER"; describe = "普通用户，拥有管理员赋予的权利"  })
        roleService.saveBatch(batchList)

        return superAdminId
    }

    /**
     * 初始化超级管理员菜单权限
     * @param superAdminId Long     超级管理员id
     */
    fun initRoleMenu(superAdminId: Long) {
        val batchList: MutableList<SysRoleMenu> = mutableListOf()
        menuService.list().map { it.id!! }.forEach {
            batchList.add(SysRoleMenu().apply { roleId = superAdminId; menuId = it })
        }

        roleMenuService.saveBatch(batchList)
    }

    /**
     * 初始化超级管理员用户
     */
    fun initAdminUser(): Long {
        val userId = uidGenerator.getUid()
        val passwordEncoder = BCrypt.hashpw("admin")
        userService.save(SysUser().apply {
            id = userId
            username = "zeta管理员"
            account = "zetaAdmin"
            password = passwordEncoder
            sex = SexEnum.MALE.code
            state = UserStateEnum.NORMAL.code
            readonly = true
        })

        return userId
    }

    /**
     * 初始化用户角色
     * @param userId Long           用户id
     * @param superAdminId Long     角色（超级管理员）id
     */
    fun initUserRole(userId: Long, superAdminId: Long) {
        userRoleService.save(SysUserRole(userId, superAdminId))
    }

    /**
     * 初始化数据字典
     */
    fun initSysDict() {
        // 初始化字典
        val dictList = mutableListOf<SysDict>()
        val dictId = uidGenerator.getUid()
        dictList.add(SysDict().apply { id = dictId; name = "设备状态"; code = "device_status"; describe = "设备运行状态"; sortValue = 0 })
        sysDictService.saveBatch(dictList)

        // 初始化字典项
        val dictItemList = mutableListOf<SysDictItem>()
        dictItemList.add(SysDictItem().apply { this.id = uidGenerator.getUid(); this.dictId = dictId; name = "运行"; value = "RUNNING";  describe = "设备正在运行"; sortValue = 1 })
        dictItemList.add(SysDictItem().apply { this.id = uidGenerator.getUid(); this.dictId = dictId; name = "停止"; value = "WAITING";  describe = "设备已停止"; sortValue = 2 })
        sysDictItemService.saveBatch(dictItemList)
    }

    /**
     * 构造菜单
     *
     * @param id
     * @param parentId
     * @param sortValue
     * @param title
     * @param path
     * @param icon
     */
    private fun buildMenu(id: Long, parentId: Long, sortValue: Int, title: String, type: MenuTypeEnum, path: String? = null, icon: String? = null): SysMenu {
        // 判断组件
        return SysMenu().apply {
            this.id = id
            this.parentId = parentId
            this.sortValue = sortValue
            this.title = title
            this.icon = icon
            this.type = type.code
            this.authority = ""
            this.openType = if (type == MenuTypeEnum.MENU) "_iframe" else ""
            this.href = path
        }
    }

    /**
     * 构造按钮
     *
     * @param id
     * @param parentId
     * @param sortValue
     * @param title
     * @param authority
     */
    private fun buildButton(id: Long, parentId: Long, sortValue: Int, title: String, authority: String): SysMenu {
        return SysMenu().apply {
            this.id = id
            this.parentId = parentId
            this.sortValue = sortValue
            this.title = title
            this.type = MenuTypeEnum.RESOURCE.code
            this.authority = authority
        }
    }

}
