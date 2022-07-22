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
import ${package.Entity?replace('api.entity', 'api.vo.*')};
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

    @ApiOperation(value = "分页查询")
    @PostMapping("findPage")
    public IPage<${entity?replace('Entity', 'ResVO')}> findPage(@RequestBody ${entity?replace('Entity', 'FindPageReqVO')} reqVO) {
        return ${table.serviceName?uncap_first}.findPage(reqVO);
    }

    @ApiOperation(value = "单个查询")
    @PostMapping("findById")
    public ${entity?replace('Entity', 'ResVO')} findById(@RequestBody ${entity?replace('Entity', 'IdReqVO')} reqVO) {
        return ${table.serviceName?uncap_first}.findById(reqVO.getId());
    }

    @ApiOperation(value = "restful单个查询")
    @GetMapping("find/{id}")
    public ${entity?replace('Entity', 'ResVO')} find(@PathVariable("id") Long id) {
        return ${table.serviceName?uncap_first}.findById(id);
    }

    @ApiOperation(value = "唯一标识单个查询")
    @PostMapping("findByCode")
    public ${entity?replace('Entity', 'ResVO')} findByCode(@RequestBody ${entity?replace('Entity', 'CodeReqVO')} reqVO) {
        return ${table.serviceName?uncap_first}.findByCode(reqVO);
    }

    @ApiOperation(value = "查询list")
    @PostMapping("findList")
    public List<${entity?replace('Entity', 'ResVO')}> findList(@RequestBody ${entity?replace('Entity', 'FindListReqVO')} reqVO) {
        return ${table.serviceName?uncap_first}.findList(reqVO);
    }

    @ApiOperation(value = "新增")
    @PostMapping("create")
    public void create(@RequestBody ${entity?replace('Entity', 'CreateReqVO')} reqVO) {
        ${table.serviceName?uncap_first}.create(reqVO);
    }

    @ApiOperation(value = "删除")
    @PostMapping("deleteById")
    public void deleteById(@RequestBody ${entity?replace('Entity', 'IdReqVO')} reqVO) {
        ${table.serviceName?uncap_first}.deleteById(reqVO);
    }

    @ApiOperation(value = "restful删除")
    @DeleteMapping("delete/{id}")
    public void delete(@PathVariable("id") Long id) {
        ${table.serviceName?uncap_first}.deleteById(id);
    }

    @ApiOperation(value = "更新")
    @PostMapping("updateById")
    public void updateById(@RequestBody ${entity?replace('Entity', 'UpdateReqVO')} reqVO) {
        ${table.serviceName?uncap_first}.update(reqVO);
    }

    @ApiOperation(value = "restful更新")
    @PutMapping("update")
    public void update(@RequestBody ${entity?replace('Entity', 'UpdateReqVO')} reqVO) {
        ${table.serviceName?uncap_first}.update(reqVO);
    }
}
</#if>
