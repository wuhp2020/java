package com.spring.global.handler;

import com.google.gson.Gson;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonInputMessage;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class GlobalRequestHandler implements RequestBodyAdvice {

    @Value("${swagger.urls}")
    private List<String> swaggerUrls;

    private Gson gson = new Gson();

    /**
     * 该方法用于判断当前请求, 是否要执行beforeBodyRead, afterBodyRead方法
     * @param methodParameter
     * @param type
     * @param aClass
     * @return
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        //判断方法的参数含有RequestBody注解的才执行beforeBodyRead, afterBodyRead
        Boolean supported = methodParameter.getParameterAnnotation(RequestBody.class) != null;
        return supported;
    }

    /**
     * 读取参数前执行
     * @param httpInputMessage
     * @param methodParameter
     * @param type
     * @param aClass
     * @return
     * @throws IOException
     */
    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage httpInputMessage, MethodParameter methodParameter,
                 Type type, Class<? extends HttpMessageConverter<?>> aClass) throws IOException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String contextPath = request.getContextPath();
        if (swaggerUrls.contains(contextPath)) {
            return httpInputMessage;
        }

        HttpInputMessage ret = null;
        String requestDTO = StreamUtils.copyToString(httpInputMessage.getBody(), StandardCharsets.UTF_8);
        if (Objects.nonNull(requestDTO)) {
            ByteArrayInputStream is = new ByteArrayInputStream(requestDTO.getBytes());
            ret = new MappingJacksonInputMessage(is, httpInputMessage.getHeaders());
            is.close();
        }
        log.info("=====>>>>> 接收报文: {}", gson.toJson(requestDTO));
        return ret;
    }

    /**
     * 读取参数后执行
     * @param o
     * @param httpInputMessage
     * @param methodParameter
     * @param type
     * @param aClass
     * @return
     */
    @SneakyThrows
    @Override
    public Object afterBodyRead(Object o, HttpInputMessage httpInputMessage, MethodParameter methodParameter,
             Type type, Class<? extends HttpMessageConverter<?>> aClass) {
       return o;
    }

    /**
     * 无请求时的处理
     * @param o
     * @param httpInputMessage
     * @param methodParameter
     * @param type
     * @param aClass
     * @return
     */
    @Override
    public Object handleEmptyBody(Object o, HttpInputMessage httpInputMessage, MethodParameter methodParameter,
            Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return o;
    }
}

