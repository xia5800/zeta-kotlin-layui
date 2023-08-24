package com.zeta.system.controller

import cn.afterturn.easypoi.excel.entity.ImportParams
import cn.hutool.core.bean.BeanUtil
import com.zeta.system.model.dto.sysDict.SysDictSaveDTO
import com.zeta.system.model.dto.sysDict.SysDictUpdateDTO
import com.zeta.system.model.entity.SysDict
import com.zeta.system.model.param.SysDictQueryParam
import com.zeta.system.model.poi.SysDictExportPoi
import com.zeta.system.model.poi.SysDictImportPoi
import com.zeta.system.poi.SysDictExcelVerifyHandler
import com.zeta.system.service.ISysDictService
import io.swagger.annotations.Api
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.zetaframework.base.controller.SuperAllViewController
import org.zetaframework.base.controller.extra.ExistenceController
import org.zetaframework.base.controller.extra.PoiController
import org.zetaframework.base.param.ExistParam
import org.zetaframework.base.result.ApiResult
import org.zetaframework.core.exception.BusinessException
import org.zetaframework.core.saToken.annotation.PreAuth
import javax.servlet.http.HttpServletRequest

/**
 * 字典 前端控制器
 *
 * @author AutoGenerator
 * @date 2022-04-15 10:12:09
 */
@Api(tags = ["字典"])
@PreAuth(replace = "sys:dict")
@Controller
@RequestMapping("/system/dict")
class SysDictController(
    private val sysDictExcelVerifyHandler: SysDictExcelVerifyHandler
) : SuperAllViewController<ISysDictService, Long, SysDict, SysDictQueryParam, SysDictSaveDTO, SysDictUpdateDTO>(),
    ExistenceController<SysDict, Long>,
    PoiController<SysDictImportPoi, SysDictExportPoi, SysDict, SysDictQueryParam>
{

    /**
     * 当前模块路径
     */
    override fun getModulePath(): String {
        return "/system/dict"
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
    override fun beforeUpdateView(model: Model, entity: SysDict?, request: HttpServletRequest) {
        // 获取字典数据
        val dict = entity?.id?.let {
            service.getById(it)
        } ?: throw BusinessException("字典不存在")

        model.addAttribute("dict", dict)
    }


    /**
     * 自定义新增
     *
     * @param saveDTO SaveDTO 保存对象
     * @return ApiResult<Boolean>
     */
    override fun handlerSave(saveDTO: SysDictSaveDTO): ApiResult<Boolean> {
        // 判断是否存在
        if (ExistParam<SysDict, Long>(SysDict::code, saveDTO.code).isExist(service)) {
            return fail("编码已存在")
        }
        return super.handlerSave(saveDTO)
    }

    /**
     * 自定义修改
     *
     * @param updateDTO UpdateDTO 修改对象
     * @return ApiResult<Boolean>
     */
    override fun handlerUpdate(updateDTO: SysDictUpdateDTO): ApiResult<Boolean> {
        // 判断是否存在
        if (ExistParam<SysDict, Long>(SysDict::code, updateDTO.code, updateDTO.id).isExist(service)) {
            return fail("编码已存在")
        }
        return super.handlerUpdate(updateDTO)
    }

    /**
     * 导入参数增强
     *
     * 说明：
     * 你可以在这里对ImportParams配置进行一些补充
     * 例如设置excel验证规则、校验组、校验处理接口等
     */
    override fun enhanceImportParams(importParams: ImportParams) {
        // 开启：校验上传的Excel数据
        importParams.isNeedVerify = true
        // 校验处理接口：字典编码重复校验
        importParams.verifyHandler = sysDictExcelVerifyHandler
    }

    /**
     * 处理导入数据
     *
     * 说明：
     * 你需要手动实现导入逻辑
     */
    override fun handlerImport(list: MutableList<SysDictImportPoi>): ApiResult<Boolean> {
        val batchList: List<SysDict> = list.map {
            BeanUtil.toBean(it, SysDict::class.java)
        }
        return success(service.saveBatch(batchList))
    }

    /**
     * 获取待导出的数据
     *
     * @param param QueryParam
     * @return MutableList<ExportBean>
     */
    override fun findExportList(param: SysDictQueryParam): MutableList<SysDictExportPoi> {
        // 条件查询Entity数据
        val list = super.handlerBatchQuery(param)
        if (list.isEmpty()) return mutableListOf()

        // Entity -> ExportBean
        return list.map {
            BeanUtil.toBean(it, SysDictExportPoi::class.java)
        }.toMutableList()
    }

}
