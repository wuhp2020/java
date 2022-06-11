package com.spring.aspect;

import com.google.gson.Gson;
import com.spring.config.WebSecurityConfig;
import com.web.model.OperationLogDO;
import com.web.repository.OperationLogRepository;
import com.web.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Aspect
@Component
@Slf4j
@Order(9)
public class LogInfoAspect {

    @Autowired
    private OperationLogRepository operationLogRepository;

    @Pointcut("execution(public * com.web.controller.*.*(..)) ")
    private void anyMethod() {
    }

    @Around("anyMethod()")
    public <T> Object doAccessCheck(ProceedingJoinPoint joinPoint) throws Throwable {
        try {

            HttpServletRequest request =
                    ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

            OperationLogDO operationLogDO = new OperationLogDO();
            operationLogDO.setClientIP(request.getRemoteAddr());
            operationLogDO.setRequestTime(new Date());
            String requestURL = request.getRequestURL().toString();
            operationLogDO.setRequestURL(
                    requestURL.substring(StringUtil.getCharacterPosition(requestURL, "/", 3)));
            WebSecurityConfig.SecurityUser securityUser = WebSecurityConfig.getLoginUser();
            operationLogDO.setUsername(securityUser.getUsername());
            Object[] args = joinPoint.getArgs();
            Gson gson = new Gson();
            operationLogDO.setInputParams(gson.toJson(args));

            Object object = joinPoint.proceed();
            operationLogDO.setOutputParams(gson.toJson(object));

            // 日志查询不保存
            if(!requestURL.contains("operationlog")){
                operationLogRepository.save(operationLogDO);
            }

            return object;

        } catch (Exception e){
            e.printStackTrace();
            return joinPoint.proceed();
        }
    }
}
