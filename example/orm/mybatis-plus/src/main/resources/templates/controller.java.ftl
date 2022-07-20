package ${package.Controller};

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ${package.Service}.${table.serviceName};
import ${package.Entity}.${entity};
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.sugon.address.api.vo.*;
<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>

/**
 * ${table.comment!}
 * @author ${author}
 * @date ${date}
 */
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("<#if package.ModuleName?? && package.ModuleName != "">/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
@Api(tags = {"${entity}管理"})
public class ${table.controllerName} {
</#if>

    @Autowired
    private ${table.serviceName} ${table.serviceName?uncap_first};

    @ApiOperation(value = "查询分页数据")
    @PostMapping("findPage")
    public IPage<${entity?replace('Entity', 'ResVO')}> findPage(@RequestBody ${entity?replace('Entity', 'FindPageReqVO')} reqVO) {
        return ${table.serviceName?uncap_first}.findPage(reqVO);
    }

    @ApiOperation(value = "查询单个数据")
    @PostMapping("findById")
    public ${entity?replace('Entity', 'ResVO')} findById(@RequestBody ${entity?replace('Entity', 'IdReqVO')} reqVO) {
        return ${table.serviceName?uncap_first}.findById(reqVO.getId());
    }

    @ApiOperation(value = "查询数据集")
    @PostMapping("findList")
    public List<${entity?replace('Entity', 'ResVO')}> findList(@RequestBody ${entity?replace('Entity', 'FindListReqVO')} reqVO) {
        return ${table.serviceName?uncap_first}.findList(reqVO);
    }

    @ApiOperation(value = "新增数据")
    @PostMapping("create")
    public void create(@RequestBody ${entity?replace('Entity', 'CreateReqVO')} reqVO) {
        ${table.serviceName?uncap_first}.create(reqVO);
    }

    @ApiOperation(value = "删除数据")
    @PostMapping("deleteById")
    public void delete(@RequestBody ${entity?replace('Entity', 'IdReqVO')} reqVO) {
        ${table.serviceName?uncap_first}.delete(reqVO);
    }

    @ApiOperation(value = "更新数据")
    @PostMapping("updateById")
    public void update(@RequestBody ${entity?replace('Entity', 'UpdateReqVO')} reqVO) {
        ${table.serviceName?uncap_first}.update(reqVO);
    }
}
</#if>
