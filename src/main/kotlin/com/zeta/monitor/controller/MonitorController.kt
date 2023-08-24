package com.zeta.monitor.controller

import com.aizuda.monitor.OshiMonitor
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport
import com.zeta.monitor.model.ServerInfoDTO
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.zetaframework.base.controller.SuperBaseController
import org.zetaframework.base.controller.goView
import org.zetaframework.base.result.ApiResult
import org.zetaframework.core.log.annotation.SysLog
import springfox.documentation.annotations.ApiIgnore
import java.util.*
import javax.servlet.http.HttpServletRequest


/**
 * 系统监控
 *
 * @author gcc
 */
@Api(tags = ["系统监控"])
@Controller
@RequestMapping("/monitor")
class MonitorController(
    private val oshiMonitor: OshiMonitor,
    private val redisTemplate: StringRedisTemplate
) : SuperBaseController {

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
        return "/monitor"
    }

    /**
     * 主页
     *
     * @param model [Model]
     * @param request 请求对象
     */
    @ApiIgnore
    @GetMapping("/index")
    fun indexView(model: Model, request: HttpServletRequest): String {
        return goView("index.html")
    }

    /**
     * 获取服务器信息
     *
     * @return ApiResult<Boolean>
     */
    @ApiOperationSupport(order = 10)
    @ApiOperation(value = "服务器信息")
    @SysLog
    @ResponseBody
    @GetMapping("/server")
    fun getServerInfo(): ApiResult<ServerInfoDTO> {
        return success(ServerInfoDTO().apply {
            // 系统信息
            this.sysInfo = ServerInfoDTO.SysInfo.build(oshiMonitor.sysInfo)
            // CPU使用率信息
            this.cpuInfo = ServerInfoDTO.CpuInfo.build(oshiMonitor.cpuInfo)
            // 内存信息
            this.memoryInfo = ServerInfoDTO.MemoryInfo.build(oshiMonitor.memoryInfo)
            // Jvm 虚拟机信息
            this.jvmInfo = ServerInfoDTO.JvmInfo.build(oshiMonitor.jvmInfo)
            // CPU信息
            this.centralProcessor = ServerInfoDTO.CentralProcessor.build(
                oshiMonitor.centralProcessor.processorIdentifier
            )
            // 磁盘信息
            this.diskInfos = ServerInfoDTO.DiskInfo.build(oshiMonitor.diskInfos)
        })
    }

    /**
     * 获取Redis信息
     *
     * @return ApiResult<Boolean>
     */
    @ApiOperationSupport(order = 20)
    @ApiOperation(value = "Redis信息")
    @SysLog
    @ResponseBody
    @GetMapping("/redis")
    fun getRedisInfo(): ApiResult<Properties?> {
        val properties: Properties? = redisTemplate.connectionFactory?.connection?.info()
        return success(data = properties)
    }

}
