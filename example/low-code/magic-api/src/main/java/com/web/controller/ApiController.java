package com.web.controller;

import com.web.convert.DataMapper;
import com.web.service.IScriptService;
import com.web.vo.api.CreateApiVO;
import com.web.vo.api.CreateGroupVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ssssssss.magicapi.core.config.MagicConfiguration;
import org.ssssssss.magicapi.core.model.ApiInfo;
import org.ssssssss.magicapi.core.model.Group;
import org.ssssssss.magicapi.core.model.TreeNode;
import org.ssssssss.magicapi.core.service.MagicAPIService;

import java.util.List;

/**
 * @ Author : wuheping
 * @ Date   : 2022/5/26
 * @ Desc   : 描述
 */
@RestController
@RequestMapping("/api/v1")
@Api(tags = "接口管理")
@Slf4j
public class ApiController {

    @Autowired
    private MagicAPIService service;

    @Autowired
    private List<IScriptService> iScriptServices;

    @PostMapping("createApi")
    @ApiOperation(value = "创建接口")
    public void createApi(@RequestBody CreateApiVO createApiVO) {
        ApiInfo apiInfo = DataMapper.INSTANCES.create2ApiInfo(createApiVO);
        // 根据方法类型、入参、出参生成增删改查逻辑
        IScriptService iScriptService = iScriptServices.stream()
                .filter(service -> service.method().equals(createApiVO.getMethod()))
                .findFirst().get();
        apiInfo.setScript(iScriptService.script(createApiVO));
        boolean result = MagicConfiguration.getMagicResourceService().saveFile(apiInfo);
    }

    @PostMapping("createGroup")
    @ApiOperation(value = "创建组")
    public void createGroup(@RequestBody CreateGroupVO createGroupVO) {
        Group group = DataMapper.INSTANCES.create2Group(createGroupVO);
        boolean result = MagicConfiguration.getMagicResourceService().saveGroup(group);
    }

    @PostMapping("findGroup")
    @ApiOperation(value = "查询组")
    public TreeNode<Group> findGroup() {
        return MagicConfiguration.getMagicResourceService().tree("api");
    }

    @PostMapping("findListFiles/{groupId}")
    @ApiOperation(value = "查询组下接口")
    public List findListFiles(@PathVariable("groupId") String groupId) {
        return MagicConfiguration.getMagicResourceService().listFiles(groupId);
    }
}
