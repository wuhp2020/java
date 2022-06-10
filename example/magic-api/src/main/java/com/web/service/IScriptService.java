package com.web.service;

import com.web.vo.api.CreateApiVO;

/**
 * @ Author : wuheping
 * @ Date   : 2022/5/28
 * @ Desc   : 描述
 */
public interface IScriptService {

    public String script(CreateApiVO createApiVO);

    public String method();
}
