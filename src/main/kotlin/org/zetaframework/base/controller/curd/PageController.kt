package org.zetaframework.base.controller.curd

import cn.hutool.core.bean.BeanUtil
import cn.hutool.core.util.StrUtil
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.core.metadata.IPage
import org.zetaframework.base.controller.BaseController
import org.zetaframework.base.param.PageParam
import org.zetaframework.base.result.PageResult

/**
 * 分页 Controller
 *
 * @param <Entity>     实体
 * @param <QueryParam>  查询参数
 * @author gcc
 */
interface PageController<Entity, QueryParam>: BaseController<Entity> {

    /**
     * 分页查询
     *
     * @param pageParam PageParam 分页参数：page、limit、order、sort等
     * @param queryParam QueryParam 查询条件
     * @return IPage<Entity>
     */
    fun query(pageParam: PageParam, queryParam: QueryParam?): PageResult<Entity> {
        // 处理查询参数
        handlerQueryParams(pageParam, queryParam)

        // 构建分页对象
        val page: IPage<Entity> = pageParam.buildPage<Entity>()
        // PageQuery -> Entity
        val model: Entity = BeanUtil.toBean(queryParam, getEntityClass())

        // 构造分页查询条件
        val wrapper = handlerWrapper(model, pageParam, queryParam)
        // 执行单表分页查询
        getBaseService().page(page, wrapper)

        // 处理查询后的分页结果
        handlerResult(page)

        return PageResult(page.records, page.total.toInt())
    }


    /**
     * 构造查询条件
     *
     * @param model Entity?
     * @param param PageParams<PageQuery>
     * @return QueryWrapper<Entity>
     */
    fun handlerWrapper(model: Entity?, pageParam: PageParam? = null, queryParam: QueryParam? = null): QueryWrapper<Entity> {
        // ?.let 不为空执行
        return model?.let { QueryWrapper<Entity>(model) } ?: QueryWrapper<Entity>()
    }


    /**
     * 处理查询参数
     *
     * @param param 查询参数
     */
    fun handlerQueryParams(pageParam: PageParam, queryParam: QueryParam?) {
        // 默认排序
        if (StrUtil.isBlank(pageParam.sort)) {
            pageParam.setDefaultDesc("id")
        }
    }

    /**
     * 处理查询后的数据
     *
     * @param page IPage
     */
    fun handlerResult(page: IPage<Entity>) { }

}
