package org.zetaframework.core.beetl.function

import cn.dev33.satoken.stp.StpUtil
import cn.hutool.core.bean.BeanUtil
import com.zeta.system.model.dto.sysUser.LoginUserDTO

/**
 * 扩展Beetl方法
 *
 * 说明：
 * 1.前端按钮鉴权
 * 2.为了前端方便从（SaToken的）session中获取数据
 *
 * 使用方式：
 * ```
 * // 方式一：在beetl模板中使用
 * <%
 *  if(sa.hasPermission("system:user:view")) {
 *      println("xxxxx");
 *  }
 * %>
 *
 * // 方式二：判断按钮是否显示
 * <:if test="${sa.hasRole('SUPER_ADMIN')}">
 *      <button ...>添加</button>
 * </:if>
 *
 * // 方式三：获取用户名
 * <span>${sa.loginUser('username')!''}</span>
 *
 * ```
 *
 * @author gcc
 */
class SaFunctionPackage {

    /**
     * 判断：当前账号是否含有指定权限, 返回true或false
     * @param permission 权限码
     * @return 是否含有指定权限
     */
    fun hasPermission(permission: String): Boolean {
        return StpUtil.hasPermission(permission)
    }

    /**
     * 判断：当前账号是否含有指定权限, [指定多个，必须全部具有]
     * @param permissionArray 权限码数组
     * @return true 或 false
     */
    fun hasPermissionAnd(vararg permissionArray: String): Boolean {
        return StpUtil.hasPermissionAnd(*permissionArray)
    }

    /**
     * 判断：当前账号是否含有指定权限 [指定多个，只要其一验证通过即可]
     * @param permissionArray 权限码数组
     * @return true 或 false
     */
    fun hasPermissionOr(vararg permissionArray: String): Boolean {
        return StpUtil.hasPermissionOr(*permissionArray)
    }

    /**
     * 判断：当前账号是否拥有指定角色, 返回true或false
     * @param role 角色标识
     * @return 是否含有指定角色标识
     */
    fun hasRole(role: String): Boolean {
        return StpUtil.hasRole(role)
    }

    /**
     * 判断：当前账号是否含有指定角色标识 [指定多个，必须全部验证通过]
     * @param roleArray 角色标识数组
     * @return true或false
     */
    fun hasRoleAnd(vararg roleArray: String): Boolean {
        return StpUtil.hasRoleAnd(*roleArray)
    }

    /**
     * 判断：当前账号是否含有指定角色标识 [指定多个，只要其一验证通过即可]
     * @param roleArray 角色标识数组
     * @return true或false
     */
    fun hasRoleOr(vararg roleArray: String): Boolean {
        return StpUtil.hasRoleOr(*roleArray)
    }

    /**
     * 当前登录用户（基本信息）
     * @return LoginUserDTO
     */
    fun loginUser(): LoginUserDTO? {
        return StpUtil.getSession().get("user") as LoginUserDTO?
    }

    /**
     * 当前登录用户的属性值
     *
     * @param property 用户属性名
     * @return String
     */
    fun loginUser(property: String): Any? {
        val user = loginUser() ?: return null
        val beanToMap = BeanUtil.beanToMap(user)
        return beanToMap.getOrDefault(property, null)
    }

}
