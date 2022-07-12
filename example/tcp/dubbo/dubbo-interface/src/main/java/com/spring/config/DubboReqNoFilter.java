package com.spring.config;

import com.alibaba.dubbo.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcException;

import java.util.UUID;

/**
 * @ Author : wuheping
 * @ Date   : 2022/4/29
 * @ Desc   : 描述
 */
@Slf4j
@Activate(group = Constants.CONSUMER, order = -30000)
public class DubboReqNoFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        try {
            Object[] arguments = invocation.getArguments();
            if (arguments != null && arguments.length > 0) {
                Object obj = arguments[0];
            }
        } catch (Exception e) {
            log.error("链路失败, 不影响业务: ", e);
        }
        return invoker.invoke(invocation);
    }
}
