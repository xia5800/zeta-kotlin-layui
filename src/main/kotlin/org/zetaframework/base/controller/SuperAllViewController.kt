package org.zetaframework.base.controller

import com.baomidou.mybatisplus.extension.service.IService
import org.zetaframework.base.controller.view.SaveView
import org.zetaframework.base.controller.view.UpdateView
import java.io.Serializable

/**
 * 完整增删改查 BaseController
 *
 * 实现了Query、Save、Update、Delete、BatchDelete, IndexView, SaveView, UpdateView
 *
 * 说明：
 * 适用于“新增、修改、列表页面都是独立页面”这种情况
 *
 * @param <S>           Service
 * @param <Id>          主键字段类型
 * @param <Entity>      实体
 * @param <QueryParam>  查询参数
 * @param <SaveDTO>     保存对象
 * @param <UpdateDTO>   修改对象
 * @author gcc
 */
abstract class SuperAllViewController<S: IService<Entity>, Id: Serializable, Entity, QueryParam, SaveDTO, UpdateDTO> :
    SuperController<S, Id, Entity, QueryParam, SaveDTO, UpdateDTO>(),
    SaveView<Entity>,
    UpdateView<Entity>
{

}
