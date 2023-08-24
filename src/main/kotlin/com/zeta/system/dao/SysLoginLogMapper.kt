package com.zeta.system.dao

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.baomidou.mybatisplus.core.metadata.IPage
import com.zeta.system.model.entity.SysLoginLog
import com.zeta.system.model.param.SysLoginLogQueryParam
import org.apache.ibatis.annotations.Param
import org.springframework.stereotype.Repository

/**
 * 登录日志 Mapper 接口
 *
 * @author AutoGenerator
 * @date 2022-03-21 16:33:13
 */
@Repository
interface SysLoginLogMapper : BaseMapper<SysLoginLog> {

    /**
     * 自定义分页查询登录日志
     *
     * 说明：
     * 因为默认的分页方法不支持时间范围查询，故自定义sql来分页查询
     *
     * @param page
     * @param param
     */
    fun customPage(
        @Param("page") page: IPage<SysLoginLog>,
        @Param("param") param: SysLoginLogQueryParam
    ): List<SysLoginLog>

}
