package com.web.controller;

import com.web.service.RoleService;
import com.web.vo.common.ResponseVO;
import com.web.vo.role.RoleQueryVO;
import com.web.vo.role.RoleVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/role")
@Slf4j
@Api(tags = "角色管理")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping("save")
    @ApiOperation(value = "增加角色")
    public ResponseVO save(RoleVO roleVO) {
        try {
            // 校验
            Pair<Boolean, String> result = roleService.checkRepeat(roleVO);
            if (!result.getFirst()) {
                return ResponseVO.FAIL(result.getSecond());
            }
            roleVO = roleService.save(roleVO);
            return ResponseVO.SUCCESS(roleVO);
        } catch (Exception e) {
            log.error("class:RoleController, method:save 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @DeleteMapping("delete/{ids}")
    @ApiOperation(value = "删除角色")
    public ResponseVO delete(@PathVariable("ids") List<String> ids) {
        try {
            Pair<Boolean, String> result = roleService.delete(ids);
            if (result.getFirst()) {
                return ResponseVO.SUCCESS(result.getSecond());
            } else {
                return ResponseVO.FAIL(result.getSecond());
            }
        } catch (Exception e) {
            log.error("class:RoleController, method:delete 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @PutMapping("update")
    @ApiOperation(value = "修改角色")
    public ResponseVO update(RoleVO roleVO) {
        try {
            Pair<Boolean, String> result = roleService.roleIdNull(roleVO);
            if (!result.getFirst()) {
                return ResponseVO.FAIL(result.getSecond());
            }
            roleVO = roleService.update(roleVO);
            return ResponseVO.SUCCESS(roleVO);
        } catch (Exception e) {
            log.error("class:RoleController, method:update 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @GetMapping("findOne/{id}")
    @ApiOperation(value = "查询单个角色")
    public ResponseVO findOne(@PathVariable("id") String id) {
        try {
            RoleVO roleVO = roleService.findOne(id);
            return ResponseVO.SUCCESS(roleVO);
        } catch (Exception e) {
            log.error("class:RoleController, method:findOne 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @PostMapping("findByPage")
    @ApiOperation(value = "分页查询角色")
    public ResponseVO findByPage(RoleQueryVO roleQueryVO) {
        try {
            Page<RoleVO> page = roleService.findByPage(roleQueryVO);
            return ResponseVO.SUCCESS(page);
        } catch (Exception e) {
            log.error("class:RoleController, method:findByPage 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

}
