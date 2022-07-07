package com.web.controller;

import com.web.service.DimissionService;
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

@RestController
@RequestMapping("/api/v1/dimission")
@Api(tags = "离职管理")
@Slf4j
public class DimissionController {

    @Autowired
    private DimissionService dimissionService;

    @ApiOperation(value = "发起请假流程")
    @PostMapping("start")
    public ResponseVO startLeave(@RequestBody EmployeeVO employeeVO) {
        try {
            dimissionService.startLeave(employeeVO);
            return ResponseVO.SUCCESS("");
        } catch (Exception e) {
            log.error("发起请假流程失败", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @ApiOperation(value = "查询需要自己审批的流程")
    @GetMapping("find/{leadername}")
    public ResponseVO findByLeaderName(@PathVariable("leadername") String leadername) {
        try {
            return ResponseVO.SUCCESS(dimissionService.findByLeaderName(leadername));
        } catch (Exception e) {
            log.error("查询需要自己审批的流程失败", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @ApiOperation(value = "审批自己的流程")
    @PostMapping("examine")
    public ResponseVO examineLeave(String taskId) {
        try {
            dimissionService.examineLeave(taskId);
            return ResponseVO.SUCCESS(null);
        } catch (Exception e) {
            log.error("审批自己的流程失败", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @ApiOperation(value = "自己的历史流程")
    @GetMapping("find/history/{username}")
    public ResponseVO historyLeave(@PathVariable("username") String username) {
        try {
            return ResponseVO.SUCCESS(dimissionService.historyLeave(username));
        } catch (Exception e) {
            log.error("审批自己的流程失败", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @ApiOperation(value = "获取流程图")
    @GetMapping("find/activiti/{instanceid}")
    public ResponseVO activiti(@PathVariable("instanceid") String instanceid) {
        try {
            return ResponseVO.SUCCESS(dimissionService.activiti(instanceid));
        } catch (Exception e) {
            log.error("获取流程图失败", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }
}
