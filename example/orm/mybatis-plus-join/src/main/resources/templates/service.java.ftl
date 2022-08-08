package ${package.Service};

import ${package.Entity}.${entity};
import ${superServiceClassPackage};
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.List;
import ${package.Entity?replace('api.entity', 'api.vo.*')};

/**
 * ${table.comment!}
 * @author ${author}
 * @date ${date}
 */
<#if kotlin>
interface ${table.serviceName} : ${superServiceClass}<${entity}>
<#else>
public interface ${table.serviceName} extends ${superServiceClass}<${entity}> {

    // 查询
    public IPage<${entity?replace('Entity', 'ResVO')}> findByPage(${entity?replace('Entity', 'FindPageReqVO')} reqVO);
    public ${entity?replace('Entity', 'ResVO')} findById(Long id);
    public ${entity?replace('Entity', 'ResVO')} findByCode(${entity?replace('Entity', 'CodeReqVO')} reqVO);
    public List<${entity?replace('Entity', 'ResVO')}> findList(${entity?replace('Entity', 'FindListReqVO')} reqVO);


    // 新增
    public void create(${entity?replace('Entity', 'CreateReqVO')} reqVO);
    public void createByList(List<${entity?replace('Entity', 'CreateReqVO')}> reqVOs);


    // 删除
    public void deleteById(Long id);


    // 修改
    public void update(${entity?replace('Entity', 'UpdateReqVO')} reqVO);
    public void updateByList(List<${entity?replace('Entity', 'UpdateReqVO')}> reqVOs);


    //新增或修改
    public void createOrUpdate(${entity?replace('Entity', 'CreateOrUpdateReqVO')} reqVO);
}
</#if>
