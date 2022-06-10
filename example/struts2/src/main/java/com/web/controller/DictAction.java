package com.web.controller;

import com.opensymphony.xwork2.ActionSupport;
import com.web.service.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.InputStream;
import java.io.OutputStream;

@Controller
@Api(tags = "字典管理")
@Slf4j
public class DictAction extends ActionSupport {

    @Autowired
    private DictService dictService;

    @Action(value = "/api/v1/dict/save")
    @ApiOperation(value = "增加字典")
    public void save() throws Exception {
        InputStream in = ServletActionContext.getRequest().getInputStream();
        OutputStream os = ServletActionContext.getResponse().getOutputStream();
        try {
            dictService.save(null);
            os.write("1".getBytes());
        } catch (Exception e) {
            os.write("2".getBytes());
        }
    }
}
