package ${package.ServiceImpl};

import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import ${package.Service}.${table.serviceName};
import ${superServiceImplClassPackage};
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import java.util.List;
import org.springframework.beans.BeanUtils;
import com.sugon.address.api.vo.*;

/**
 * ${table.comment!}
 * @author ${author}
 * @date ${date}
 */
@Service
<#if kotlin>
open class ${table.serviceImplName} : ${superServiceImplClass}<${table.mapperName}, ${entity}>(), ${table.serviceName} {

}
<#else>
public class ${table.serviceImplName} extends ${superServiceImplClass}<${table.mapperName}, ${entity}> implements ${table.serviceName} {

    @Override
    public IPage<${entity?replace('Entity', 'ResVO')}> findByPage(${entity?replace('Entity', 'FindByPageReqVO')} reqVO) {
        IPage<${entity}> wherePage = new Page(reqVO.getPageNum(), reqVO.getPageSize());
        ${entity} where = new ${entity}();
        IPage<${entity}> iPage = this.page(wherePage, Wrappers.query(where));
        // TODO 转换
        return null;
    }

    @Override
    public ${entity?replace('Entity', 'ResVO')} findById(Long id) {
        ${entity} entity = this.getById(id);
        ${entity?replace('Entity', 'ResVO')} resVO = new ${entity?replace('Entity', 'ResVO')}();
        BeanUtils.copyProperties(entity, resVO);
        return resVO;
    }

    @Override
    public List<${entity?replace('Entity', 'ResVO')}> findList(${entity?replace('Entity', 'FindListReqVO')} reqVO) {
        ${entity} ${entity?uncap_first} = new ${entity}();
        BeanUtils.copyProperties(reqVO, ${entity?uncap_first});
        Wrapper wrapperQuery = Wrappers.<${entity}>query(${entity?uncap_first}).lambda();
        List<${entity}> entitys = this.list(wrapperQuery);
        return null;
    }

    @Override
    public void create(${entity?replace('Entity', 'CreateReqVO')} reqVO) {
        ${entity} ${entity?uncap_first} = new ${entity}();
        BeanUtils.copyProperties(reqVO, ${entity?uncap_first});
        // TODO 增加ID

        this.save(${entity?uncap_first});
    }

    @Override
    public void delete(${entity?replace('Entity', 'IdReqVO')} reqVO) {
        this.removeById(reqVO.getId());
    }

    @Override
    public void update(${entity?replace('Entity', 'UpdateReqVO')} reqVO) {
        ${entity} ${entity?uncap_first} = new ${entity}();
        BeanUtils.copyProperties(reqVO, ${entity?uncap_first});
        this.updateById(${entity?uncap_first});
    }
}
</#if>