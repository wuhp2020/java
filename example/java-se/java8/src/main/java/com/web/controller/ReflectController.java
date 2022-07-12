package com.web.controller;

import com.web.service.ReflectService;
import com.web.vo.common.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

@RestController
@RequestMapping("/api/v1/reflect")
@Api(tags = "反射")
@Slf4j
public class ReflectController {

    @Autowired
    private ReflectService reflectService;

    @ApiOperation(value = "reflectClass")
    @PostMapping("reflectClass")
    public void reflectClass() throws Exception {
        Class.forName("com.web.controller.ReflectController");
        Class cls = ReflectController.class;
        new ReflectController().getClass();
    }

    @ApiOperation(value = "reflectMethod")
    @PostMapping("reflectMethod")
    public void reflectMethod() throws Exception {
        Class cls = Class.forName("com.web.controller.ReflectController");
        Constructor cons = cls.getConstructor(new Class[]{});
        Object obj = cons.newInstance(new Object[]{});
        Method method = cls.getMethod("reflectClass",new Class[]{});
        method.invoke(obj,new Object[]{});
    }

    @ApiOperation(value = "reflectConstructor")
    @PostMapping("reflectConstructor")
    public void reflectConstructor() throws Exception {
        Class cls = Class.forName("com.web.controller.ReflectController");
        Constructor cons = cls.getConstructor(new Class[]{});
        Object obj = cons.newInstance(new Object[]{});
    }
}
