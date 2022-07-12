package com.web.controller;

import com.web.service.DimissionService;
import com.web.vo.dimission.DimissionVO;
import com.web.vo.dimission.EmployeeVO;
import com.web.vo.common.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dimission")
@Api(tags = "离职管理")
@Slf4j
public class DimissionController {

    @Autowired
    private DimissionService dimissionService;

    @ApiOperation(value = "发起请假流程")
    @PostMapping("start")
    public void startLeave(@RequestBody EmployeeVO employeeVO) {
        dimissionService.startLeave(employeeVO);
    }

    @ApiOperation(value = "查询需要自己审批的流程")
    @GetMapping("find/{leadername}")
    public List<DimissionVO> findByLeaderName(@PathVariable("leadername") String leadername) {
        return dimissionService.findByLeaderName(leadername);
    }

    @ApiOperation(value = "审批自己的流程")
    @PostMapping("examine")
    public void examineLeave(String taskId) {
        dimissionService.examineLeave(taskId);
    }

    @ApiOperation(value = "自己的历史流程")
    @GetMapping("find/history/{username}")
    public List<DimissionVO> historyLeave(@PathVariable("username") String username) {
        return dimissionService.historyLeave(username);
    }

    @ApiOperation(value = "获取流程图")
    @GetMapping("find/activiti/{instanceid}")
    public void activiti(@PathVariable("instanceid") String instanceid) throws Exception {
        dimissionService.activiti(instanceid);
    }
}
