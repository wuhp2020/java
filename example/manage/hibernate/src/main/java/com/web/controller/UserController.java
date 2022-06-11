package com.web.controller;

import com.web.service.UserService;
import com.web.vo.common.ResponseVO;
import com.web.vo.user.UserQueryVO;
import com.web.vo.user.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@Slf4j
@Api(tags = "用户管理")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("save")
    @ApiOperation(value = "增加用户")
    public ResponseVO save(UserVO userVO) {
        try {
            // 校验
            Pair<Boolean, String> result = userService.checkRepeat(userVO);
            if (!result.getFirst()) {
                return ResponseVO.FAIL(result.getSecond());
            }
            userVO = userService.save(userVO);
            return ResponseVO.SUCCESS(userVO);
        } catch (Exception e) {
            log.error("class:UserController, method:save 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @DeleteMapping("delete/{ids}")
    @ApiOperation(value = "删除用户")
    public ResponseVO delete(@PathVariable("ids") List<String> ids) {
        try {
            Pair<Boolean, String> result = userService.delete(ids);
            if (result.getFirst()) {
                return ResponseVO.SUCCESS(result.getSecond());
            } else {
                return ResponseVO.FAIL(result.getSecond());
            }
        } catch (Exception e) {
            log.error("class:UserController, method:delete 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @PutMapping("update")
    @ApiOperation(value = "修改用户")
    public ResponseVO update(UserVO userVO) {
        try {
            userVO = userService.update(userVO);
            return ResponseVO.SUCCESS(userVO);
        } catch (Exception e) {
            log.error("class:UserController, method:update 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @GetMapping("findOne/{id}")
    @ApiOperation(value = "查询单个用户")
    public ResponseVO findOne(@PathVariable("id") String id) {
        try {
            UserVO userVO = userService.findOne(id);
            return ResponseVO.SUCCESS(userVO);
        } catch (Exception e) {
            log.error("class:UserController, method:findOne 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @PostMapping("findByPage")
    @ApiOperation(value = "分页查询用户")
    public ResponseVO findByPage(UserQueryVO userQueryVO) {
        try {
            Page<UserVO> page = userService.findByPage(userQueryVO);
            return ResponseVO.SUCCESS(page);
        } catch (Exception e) {
            log.error("class:UserController, method:findByPage 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

}
