package ${package.Controller};


import org.springframework.web.bind.annotation.RequestMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = {"${table.comment!}"})
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
public class ${table.controllerName} {
</#if>
    @Resource
    private ${table.serviceName} ${table.serviceName?uncap_first};

    @ApiOperation(value = "查询分页数据")
    @PostMapping("findByPage")
    public Page<${entity}> findByPage(@RequestBody ${entity} ${entity?uncap_first}) {
        return ${table.serviceName?uncap_first}.findByPage(${entity?uncap_first});
    }

    @ApiOperation(value = "查询单个数据")
    @GetMapping("find/{id}")
    public ${entity} find(@RequestParam("id") Long id) {
        return ${table.serviceName?uncap_first}.find(id);
    }

    @ApiOperation(value = "新增数据")
    @PostMapping("add")
    public void add(@RequestBody ${entity} ${entity?uncap_first}) {
        ${table.serviceName?uncap_first}.add(${entity?uncap_first});
    }

    @ApiOperation(value = "删除数据")
    @DeleteMapping("delete/{id}")
    public void delete(@RequestParam("id") Long id) {
        ${table.serviceName?uncap_first}.delete(id);
    }

    @ApiOperation(value = "更新数据")
    @PutMapping("update")
    public void update(@RequestBody ${entity} ${entity?uncap_first}) {
        ${table.serviceName?uncap_first}.update(${entity?uncap_first});
    }
}
</#if>
