package com.zeta.system.controller

import com.zeta.system.model.entity.SysFile
import com.zeta.system.model.param.SysFileQueryParam
import com.zeta.system.model.param.SysFileUploadParam
import com.zeta.system.service.ISysFileService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.springframework.stereotype.Controller
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.zetaframework.base.controller.SuperSimpleController
import org.zetaframework.base.controller.curd.DeleteController
import org.zetaframework.base.controller.curd.QueryController
import org.zetaframework.base.controller.view.IndexView
import org.zetaframework.base.result.ApiResult
import org.zetaframework.core.log.annotation.SysLog
import org.zetaframework.core.saToken.annotation.PreAuth
import org.zetaframework.core.saToken.annotation.PreCheckPermission
import org.zetaframework.core.saToken.annotation.PreMode
import javax.servlet.http.HttpServletResponse

/**
 * 系统文件 前端控制器
 *
 * @author AutoGenerator
 * @date 2022-04-11 11:18:44
 */
@Api(tags = ["系统文件"])
@PreAuth(replace = "sys:file")
@Controller
@RequestMapping("/system/file")
class SysFileController: SuperSimpleController<ISysFileService, SysFile>(),
    QueryController<SysFile, Long, SysFileQueryParam>,
    DeleteController<SysFile, Long>,
    IndexView<SysFile>
{

    /**
     * 当前模块路径
     */
    override fun getModulePath(): String {
        return "/system/file"
    }

    /**
     * 上传文件
     *
     * 吐个槽：注解地狱
     * @param file 文件对象
     * @param bizType 业务类型 例如：order、user_avatar等 会影响文件url的值
     */
    @SysLog(request = false)
    @PreCheckPermission(value = ["{}:add", "{}:save"], mode = PreMode.OR)
    @ApiOperation(value = "上传文件")
    @ResponseBody
    @PostMapping("/upload")
    fun upload(
        @RequestParam
        file: MultipartFile,
        @RequestParam(required = false)
        @ApiParam(value = "业务类型 例如：order、user_avatar等 会影响文件url的值", example = "avatar")
        bizType: String? = null
    ): ApiResult<SysFile> {
        return success(service.upload(file, bizType))
    }

    /**
     * 上传base64文件
     *
     * @param uploadParam SysFileUploadParam 上传参数
     */
    @SysLog(request = false)
    @PreCheckPermission(value = ["{}:add", "{}:save"], mode = PreMode.OR)
    @ApiOperation(value = "上传base64文件")
    @ResponseBody
    @PostMapping("/upload64")
    fun uploadBase64(@RequestBody @Validated uploadParam: SysFileUploadParam): ApiResult<SysFile> {
        return success(service.upload(uploadParam))
    }


    /**
     * 下载文件
     *
     * @param request
     * @param response
     */
    @SysLog(response = false)
    @PreCheckPermission(value = ["{}:export"])
    @ApiOperation(value = "下载文件")
    @ResponseBody
    @GetMapping(value = ["/download"], produces = ["application/octet-stream"])
    fun download(@RequestParam @ApiParam("文件id") id: Long, response: HttpServletResponse) {
        service.download(id, response)
    }

    /**
     * 自定义单体删除文件
     *
     * @param id Id
     * @return R<Boolean>
     */
    override fun handlerDelete(id: Long): ApiResult<Boolean> {
        return success(service.delete(id))
    }

    /**
     * 自定义批量删除文件
     *
     * @param ids Id
     * @return R<Boolean>
     */
    override fun handlerBatchDelete(ids: MutableList<Long>): ApiResult<Boolean> {
        return success(service.batchDelete(ids))
    }

}
