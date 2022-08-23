package com.zeta.system.controller

import cn.dev33.satoken.stp.StpUtil
import cn.hutool.core.bean.BeanUtil
import com.wf.captcha.utils.CaptchaUtil
import com.zeta.system.model.entity.SysUser
import com.zeta.system.model.enumeration.UserStateEnum
import com.zeta.system.model.param.LoginParam
import com.zeta.system.model.dto.sysUser.LoginUserDTO
import com.zeta.system.service.ISysUserService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Controller
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.zetaframework.base.controller.SuperSimpleController
import org.zetaframework.base.controller.view.DefaultView
import org.zetaframework.base.result.ApiResult
import org.zetaframework.core.exception.ArgumentException
import org.zetaframework.core.log.enums.LoginStateEnum
import org.zetaframework.core.log.event.SysLoginEvent
import org.zetaframework.core.log.model.SysLoginLogDTO
import org.zetaframework.core.utils.ContextUtil
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * 登录认证
 * @author gcc
 */
@Api(tags = ["登录认证"])
@Controller
class MainController(private val applicationContext: ApplicationContext):
    SuperSimpleController<ISysUserService, SysUser>(),
    DefaultView
{

    /**
     * 用户登录
     * @param param LoginParam
     * @return ApiResult<String>
     */
    @ApiOperation("登录")
    @ResponseBody
    @PostMapping("/login")
    fun login(@Validated param: LoginParam, request: HttpServletRequest): ApiResult<String> {
        // 验证验证码
        if (!CaptchaUtil.ver(param.code, request)) {
            return fail("验证码错误")
        }

        // 查询用户, 因为账号已经判空过了所以这里直接param.account!!
        val user = service.getByAccount(param.account!!) ?: return fail("用户不存在")
        // 设置用户id，方便记录日志的时候设置创建人。
        ContextUtil.setUserId(user.id!!)

        // 判断密码
        if(!service.comparePassword(param.password!!, user.password!!)) {
            applicationContext.publishEvent(SysLoginEvent(SysLoginLogDTO.loginFail(
                param.account!!, LoginStateEnum.ERROR_PWD, request
            )))
            // 密码不正确
            return fail(LoginStateEnum.ERROR_PWD.desc)
        }
        // 判断用户状态
        if(user.state == UserStateEnum.FORBIDDEN.code) {
            applicationContext.publishEvent(SysLoginEvent(SysLoginLogDTO.loginFail(
                param.account!!, LoginStateEnum.FAIL, "用户被禁用，无法登录", request
            )))
            return fail("用户被禁用，无法登录")
        }

        // 踢人下线并登录
        StpUtil.kickout(user.id)
        StpUtil.login(user.id, param.remember ?: false)

        // 将用户信息设置到session中去
        val userDto = BeanUtil.toBean(user, LoginUserDTO::class.java)
        StpUtil.getSession()["user"] = userDto

        // 登录日志
        applicationContext.publishEvent(SysLoginEvent(SysLoginLogDTO.loginSuccess(param.account!!, request = request)))
        return success("登录成功")
    }


    /**
     * 注销登录
     * @return ApiResult<Boolean>
     */
    @ApiOperation("注销登录")
    @ResponseBody
    @GetMapping("/logout")
    fun logout(request: HttpServletRequest): ApiResult<Boolean> {
        val user = service.getById(StpUtil.getLoginIdAsLong()) ?: throw ArgumentException("用户不存在")

        // 登出日志
        applicationContext.publishEvent(SysLoginEvent(SysLoginLogDTO.loginFail(
            user.account ?: "", LoginStateEnum.LOGOUT, request
        )))

        // 注销登录
        StpUtil.logout()
        return success("注销成功")
    }


    /**
     * 图形验证码
     *
     * @param request
     * @param response
     */
    @ApiOperation("图形验证码")
    @GetMapping("/assets/captcha")
    fun captcha(request: HttpServletRequest, response: HttpServletResponse) {
        CaptchaUtil.out(5, request, response)
    }

}
