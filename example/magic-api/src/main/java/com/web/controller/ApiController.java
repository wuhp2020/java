package com.web.controller;

import com.web.convert.DataMapper;
import com.web.service.IScriptService;
import com.web.vo.api.CreateApiVO;
import com.web.vo.api.CreateGroupVO;
import com.web.vo.common.ResponseVO;
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
    public ResponseVO createApi(@RequestBody CreateApiVO createApiVO) {
        try {
            ApiInfo apiInfo = DataMapper.INSTANCES.create2ApiInfo(createApiVO);
            // 根据方法类型、入参、出参生成增删改查逻辑
            IScriptService iScriptService = iScriptServices.stream()
                    .filter(service -> service.method().equals(createApiVO.getMethod()))
                    .findFirst().get();
            apiInfo.setScript(iScriptService.script(createApiVO));
            boolean result = MagicConfiguration.getMagicResourceService().saveFile(apiInfo);
            return ResponseVO.SUCCESS(result);
        } catch (Exception e) {
            log.error("method:createApi 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @PostMapping("createGroup")
    @ApiOperation(value = "创建组")
    public ResponseVO createGroup(@RequestBody CreateGroupVO createGroupVO) {
        try {
            Group group = DataMapper.INSTANCES.create2Group(createGroupVO);
            boolean result = MagicConfiguration.getMagicResourceService().saveGroup(group);
            return ResponseVO.SUCCESS(result);
        } catch (Exception e) {
            log.error("method:createGroup 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @PostMapping("findGroup")
    @ApiOperation(value = "查询组")
    public ResponseVO findGroup() {
        try {
            return ResponseVO.SUCCESS(MagicConfiguration.getMagicResourceService().tree("api"));
        } catch (Exception e) {
            log.error("method:findGroup 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @PostMapping("findListFiles/{groupId}")
    @ApiOperation(value = "查询组下接口")
    public ResponseVO findListFiles(@PathVariable("groupId") String groupId) {
        try {
            return ResponseVO.SUCCESS(MagicConfiguration.getMagicResourceService().listFiles(groupId));
        } catch (Exception e) {
            log.error("method:findListFiles 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }
}
