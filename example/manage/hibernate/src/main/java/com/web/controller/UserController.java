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
    public void save(UserVO userVO) throws Exception {
        // 校验
        Pair<Boolean, String> result = userService.checkRepeat(userVO);
        if (!result.getFirst()) {
            throw new RuntimeException(result.getSecond());
        }
        userVO = userService.save(userVO);
    }

    @DeleteMapping("delete/{ids}")
    @ApiOperation(value = "删除用户")
    public void delete(@PathVariable("ids") List<String> ids) {
        Pair<Boolean, String> result = userService.delete(ids);
        if (!result.getFirst()) {
            throw new RuntimeException(result.getSecond());
        }
    }

    @PutMapping("update")
    @ApiOperation(value = "修改用户")
    public void update(UserVO userVO) throws Exception {
        userVO = userService.update(userVO);
    }

    @GetMapping("findOne/{id}")
    @ApiOperation(value = "查询单个用户")
    public UserVO findOne(@PathVariable("id") String id) throws Exception {
        UserVO userVO = userService.findOne(id);
        return userVO;
    }

    @PostMapping("findByPage")
    @ApiOperation(value = "分页查询用户")
    public Page<UserVO> findByPage(UserQueryVO userQueryVO) throws Exception {
        Page<UserVO> page = userService.findByPage(userQueryVO);
        return page;
    }

}
