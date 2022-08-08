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
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;
import ${package.Entity?replace('api.entity', 'api.vo.*')};

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

    // 查询
    @Override
    public IPage<${entity?replace('Entity', 'ResVO')}> findByPage(${entity?replace('Entity', 'FindPageReqVO')} reqVO) {
        IPage<${entity}> wherePage = new Page(reqVO.getPageNum(), reqVO.getPageSize());
        ${entity} where = new ${entity}();
        IPage<${entity}> iPage = this.page(wherePage, Wrappers.query(where));
        // 转换
        Page page = new Page(reqVO.getPageNum(), reqVO.getPageSize());
        List<${entity?replace('Entity', 'ResVO')}> result = Optional.ofNullable(iPage.getRecords()).orElse(Collections.emptyList())
                .stream().map(entity -> {
                    ${entity?replace('Entity', 'ResVO')} resVO = new ${entity?replace('Entity', 'ResVO')}();
                    BeanUtils.copyProperties(entity, resVO);
                    return resVO;
                }).collect(Collectors.toList());
        // 转换
        page.setRecords(result);
        page.setCurrent(iPage.getCurrent());
        page.setSize(iPage.getSize());
        page.setTotal(iPage.getTotal());
        return page;
    }
    @Override
    public ${entity?replace('Entity', 'ResVO')} findById(Long id) {
        ${entity} entity = this.getById(id);
        ${entity?replace('Entity', 'ResVO')} resVO = new ${entity?replace('Entity', 'ResVO')}();
        BeanUtils.copyProperties(entity, resVO);
        return resVO;
    }
    @Override
    public ${entity?replace('Entity', 'ResVO')} findByCode(${entity?replace('Entity', 'CodeReqVO')} reqVO) {
        // 唯一编码
        Wrapper<${entity}> wrapper = Wrappers.<${entity}>query().lambda();
        ${entity} entity = this.getOne(wrapper);
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
        // 转换
        List<${entity?replace('Entity', 'ResVO')}> result = Optional.ofNullable(entitys).orElse(Collections.emptyList())
                .stream().map(entity -> {
                    ${entity?replace('Entity', 'ResVO')} resVO = new ${entity?replace('Entity', 'ResVO')}();
                    BeanUtils.copyProperties(entity, resVO);
                    return resVO;
                }).collect(Collectors.toList());
        return result;
    }


    // 新增
    @Override
    public void create(${entity?replace('Entity', 'CreateReqVO')} reqVO) {
        ${entity} ${entity?uncap_first} = new ${entity}();
        BeanUtils.copyProperties(reqVO, ${entity?uncap_first});
        // TODO 增加ID

        this.save(${entity?uncap_first});
    }
    @Override
    public void createByList(List<${entity?replace('Entity', 'CreateReqVO')}> reqVOs) {
        Optional.ofNullable(reqVOs).orElse(Collections.emptyList())
            .stream().forEach(reqVO -> {
                ${entity} ${entity?uncap_first} = new ${entity}();
                BeanUtils.copyProperties(reqVO, ${entity?uncap_first});
                // TODO 增加ID

                this.save(${entity?uncap_first});
        });
    }


    // 删除
    @Override
    public void deleteById(Long id) {
        this.removeById(id);
    }


    // 修改
    @Override
    public void update(${entity?replace('Entity', 'UpdateReqVO')} reqVO) {
        ${entity} ${entity?uncap_first} = new ${entity}();
        BeanUtils.copyProperties(reqVO, ${entity?uncap_first});
        this.updateById(${entity?uncap_first});
    }
    @Override
    public void updateByList(List<${entity?replace('Entity', 'UpdateReqVO')}> reqVOs) {
        Optional.ofNullable(reqVOs).orElse(Collections.emptyList())
            .stream().forEach(reqVO -> {
                ${entity} ${entity?uncap_first} = new ${entity}();
                BeanUtils.copyProperties(reqVO, ${entity?uncap_first});
                this.updateById(${entity?uncap_first});
        });
    }


    //新增或修改
    @Override
    public void createOrUpdate(${entity?replace('Entity', 'CreateOrUpdateReqVO')} reqVO) {
        if (reqVO.getId() == null || "".equals(reqVO.getId())) {
            ${entity} ${entity?uncap_first} = new ${entity}();
            BeanUtils.copyProperties(reqVO, ${entity?uncap_first});
            // TODO 增加ID

            this.save(${entity?uncap_first});
            return;
        }

        Wrapper<${entity}> wrapper = Wrappers.<${entity}>query()
                        .lambda().eq(${entity}::getId, reqVO.getId()());
        ${entity} entity = this.getOne(wrapper);
        if (entity == null) {
            ${entity} ${entity?uncap_first} = new ${entity}();
            BeanUtils.copyProperties(reqVO, ${entity?uncap_first});
            // TODO 增加ID

            this.save(${entity?uncap_first});
        } else {
            BeanUtils.copyProperties(reqVO, entity);
            this.updateById(entity);
        }
    }
}
</#if>
