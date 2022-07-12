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
    public void save(RoleVO roleVO) throws Exception {
        // 校验
        Pair<Boolean, String> result = roleService.checkRepeat(roleVO);
        if (!result.getFirst()) {
            throw  new RuntimeException(result.getSecond());
        }
        roleVO = roleService.save(roleVO);
    }

    @DeleteMapping("delete/{ids}")
    @ApiOperation(value = "删除角色")
    public void delete(@PathVariable("ids") List<String> ids) {
        Pair<Boolean, String> result = roleService.delete(ids);
        if (!result.getFirst()) {
            throw new RuntimeException(result.getSecond());
        }
    }

    @PutMapping("update")
    @ApiOperation(value = "修改角色")
    public void update(RoleVO roleVO) throws Exception {
        Pair<Boolean, String> result = roleService.roleIdNull(roleVO);
        if (!result.getFirst()) {
            throw new RuntimeException(result.getSecond());
        }
        roleVO = roleService.update(roleVO);
    }

    @GetMapping("findOne/{id}")
    @ApiOperation(value = "查询单个角色")
    public RoleVO findOne(@PathVariable("id") String id) throws Exception {
        RoleVO roleVO = roleService.findOne(id);
        return roleVO;
    }

    @PostMapping("findByPage")
    @ApiOperation(value = "分页查询角色")
    public Page<RoleVO> findByPage(RoleQueryVO roleQueryVO) throws Exception {
        Page<RoleVO> page = roleService.findByPage(roleQueryVO);
        return page;
    }

}
