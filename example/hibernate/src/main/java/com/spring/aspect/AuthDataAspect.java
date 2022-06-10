package com.spring.aspect;

import com.google.common.collect.Lists;
import com.spring.config.WebSecurityConfig;
import com.web.model.ResourceDO;
import com.web.model.RoleDO;
import com.web.model.RoleLinkedResourceDO;
import com.web.model.UserLinkedRoleDO;
import com.web.repository.ResourceRepository;
import com.web.repository.RoleRepository;
import com.web.repository.UserRepository;
import com.web.util.StringUtil;
import com.web.vo.common.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Aspect
@Component
@Slf4j
@Order(8)
public class AuthDataAspect {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Pointcut("execution(public * com.web.controller.*.*(..)) ")
    private void anyMethod() {
    }

    @Around("anyMethod()")
    public <T> Object doAccessCheck(ProceedingJoinPoint joinPoint) throws Throwable {

        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String requestURL = request.getRequestURL().toString();
        requestURL = requestURL.substring(StringUtil.getCharacterPosition(requestURL, "/", 3));

        WebSecurityConfig.SecurityUser securityUser = WebSecurityConfig.getLoginUser();
        List<UserLinkedRoleDO> userLinkedRoleDOs = securityUser.getUserLinkedRoles();
        if(!CollectionUtils.isEmpty(userLinkedRoleDOs)){
            List<String> roleIds =
                    userLinkedRoleDOs.stream().map(UserLinkedRoleDO::getRoleId).collect(Collectors.toList());
            List<RoleDO> roleDOs = (List<RoleDO>)roleRepository.findAllById(roleIds);

            if(!CollectionUtils.isEmpty(roleDOs)){
                List<String> resourceIds = Lists.newArrayList();
                for(RoleDO roleDO: roleDOs){
                    if(!CollectionUtils.isEmpty(roleDO.getRoleLinkedResources())){
                        List<String> resourceIdTemp = roleDO.getRoleLinkedResources().stream().
                                map(RoleLinkedResourceDO::getResourceId).collect(Collectors.toList());
                        resourceIds.addAll(resourceIdTemp);
                    }
                }

                List<ResourceDO> resourceDOs = (List<ResourceDO>)resourceRepository.findAllById(resourceIds);
                if(!CollectionUtils.isEmpty(resourceDOs)){
                    boolean proceedFlag = false;
                    for(ResourceDO resourceDO: resourceDOs){
                        String url = resourceDO.getUrl();
                        int index = StringUtil.getCharacterPosition(url, "[{]", 1);
                        if(index != -1){
                            url = url.substring(0, index);
                        }
                        if(requestURL.startsWith(url)){
                            proceedFlag = true;
                            break;
                        }
                    }

                    if(proceedFlag){
                        //通过
                        return joinPoint.proceed();
                    }
                }
            }
        }

        return new ResponseVO("500", "权限不足", null);
    }
}
