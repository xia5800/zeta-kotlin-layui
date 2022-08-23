package com.zeta.system.service.impl

import cn.hutool.core.bean.BeanUtil
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.zeta.system.dao.SysLoginLogMapper
import com.zeta.system.model.entity.SysLoginLog
import com.zeta.system.model.param.SysLoginLogQueryParam
import com.zeta.system.service.ISysLoginLogService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.zetaframework.base.param.PageParam
import org.zetaframework.base.result.PageResult
import org.zetaframework.core.log.model.SysLoginLogDTO

/**
 * <p>
 * 登录日志 服务实现类
 * </p>
 *
 * @author AutoGenerator
 * @date 2022-03-21 16:33:13
 */
@Service
class SysLoginLogServiceImpl: ISysLoginLogService, ServiceImpl<SysLoginLogMapper, SysLoginLog>() {
    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 保存用户登录日志
     *
     * @param loginLogDTO [SysLoginLogDTO]
     */
    override fun save(loginLogDTO: SysLoginLogDTO) {
        val loginLog = BeanUtil.toBean(loginLogDTO, SysLoginLog::class.java)
        loginLog.createdBy = loginLogDTO.userId
        this.save(loginLog)
    }

    /**
     * 自定义分页查询
     *
     * @param pageParam PageParam 分页参数
     * @param queryParam SysLoginLogQueryParam 查询条件
     */
    override fun customPage(pageParam: PageParam, queryParam: SysLoginLogQueryParam?): PageResult<SysLoginLog> {
        val page = pageParam.buildPage<SysLoginLog>()
        val loginLogList: List<SysLoginLog> = baseMapper.customPage(page, queryParam ?: SysLoginLogQueryParam())
        return PageResult(loginLogList, page.total.toInt())
    }

}
