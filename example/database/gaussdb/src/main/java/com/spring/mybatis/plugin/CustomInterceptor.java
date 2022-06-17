package com.spring.mybatis.plugin;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Slf4j
@Component
@Intercepts(@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}))
public class CustomInterceptor implements Interceptor {

    private long maxTolerate;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 拦截
        log.info("拦截成功...");
        return invocation.proceed();
    }

    @Override
    public void setProperties(Properties properties) {
        // 设置参数
        this.maxTolerate = Long.parseLong(properties.getProperty("maxTolerate"));
    }
}
