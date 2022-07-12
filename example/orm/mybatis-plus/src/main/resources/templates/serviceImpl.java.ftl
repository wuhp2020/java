package ${package.ServiceImpl};

import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import ${package.Service}.${table.serviceName};
import ${superServiceImplClassPackage};
import org.springframework.stereotype.Service;

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
    public IPage<${entity}> findByPage(${entity} ${entity?uncap_first}) {
        IPage<${entity}> wherePage = new Page<>(${entity?uncap_first}.getPageNum(), ${entity?uncap_first}.getPageSize());
        ${entity} where = new ${entity}();
        IPage<${entity}> iPage = this.page(wherePage, Wrappers.query(where));
        return iPage;
    }

    @Override
    public ${entity} find(Long id) {
        ${entity} entity = this.getById(id);
        return entity;
    }

    @Override
    public void add(${entity} ${entity?uncap_first}) {
        this.save(${entity?uncap_first});
    }

    @Override
    public void delete(Long id) {
        this.removeById(id);
    }

    @Override
    public void update(${entity} ${entity?uncap_first}) {
        this.updateById(${entity?uncap_first});
    }
}
</#if>
