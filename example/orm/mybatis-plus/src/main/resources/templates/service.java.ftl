package ${package.Service};

import ${package.Entity}.${entity};
import ${superServiceClassPackage};

/**
 * ${table.comment!}
 * @author ${author}
 * @date ${date}
 */
<#if kotlin>
interface ${table.serviceName} : ${superServiceClass}<${entity}>
<#else>
public interface ${table.serviceName} extends ${superServiceClass}<${entity}> {
    public IPage<${entity}> findByPage(${entity} ${entity?uncap_first});

    public ${entity} find(Long id);

    public void add(${entity} ${entity?uncap_first});

    public void delete(Long id);

    public void update(${entity} ${entity?uncap_first});
}
</#if>
