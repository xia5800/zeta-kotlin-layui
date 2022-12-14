package com.zeta.system.service

import com.baomidou.mybatisplus.extension.service.IService
import com.zeta.system.model.entity.SysLoginLog
import com.zeta.system.model.param.SysLoginLogQueryParam
import org.zetaframework.base.param.PageParam
import org.zetaframework.base.result.PageResult
import org.zetaframework.core.log.model.SysLoginLogDTO

/**
 * <p>
 * 登录日志 服务类
 * </p>
 *
 * @author AutoGenerator
 * @date 2022-03-21 16:33:13
 */
interface ISysLoginLogService: IService<SysLoginLog> {

    /**
     * 保存用户登录日志
     *
     * @param loginLogDTO [SysLoginLogDTO]
     */
    fun save(loginLogDTO: SysLoginLogDTO)

    /**
     * 自定义分页查询
     *
     * @param pageParam PageParam 分页参数
     * @param queryParam SysLoginLogQueryParam 查询条件
     */
    fun customPage(pageParam: PageParam, queryParam: SysLoginLogQueryParam?): PageResult<SysLoginLog>
}
