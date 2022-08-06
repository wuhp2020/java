package com.spring.global.handler;

import com.alibaba.fastjson.JSON;
import com.web.vo.common.ResponseVO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@RestControllerAdvice
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

    private static List<String> swaggerUrls;

    static {
        swaggerUrls = new ArrayList<>();
        swaggerUrls.add("/doc.html");
        swaggerUrls.add("/v2/**");
        swaggerUrls.add("/v2/api-docs");
        swaggerUrls.add("/swagger-resources");
        swaggerUrls.add("/swagger-resources/configuration/security");
        swaggerUrls.add("/swagger-resources/configuration/ui");
        swaggerUrls.add("/swagger-ui.html");
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        boolean supported = AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), ResponseBody.class)
                || returnType.hasMethodAnnotation(ResponseBody.class);
        if (!supported) {
            supported = AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), RestController.class)
                    || returnType.hasMethodAnnotation(RestController.class);
        }
        return supported;
    }

    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
              Class<? extends HttpMessageConverter<?>> selectedConverterType,
                    ServerHttpRequest request, ServerHttpResponse response) {
        String path = request.getURI().getPath();
        if (swaggerUrls.contains(path)) {
            return body;
        }
        // 可以统一处理
        log.info("=====>>>>> 返回报文: {}", body instanceof String ? body: JSON.toJSONString(body));
        if (body instanceof ResponseVO) {
            ResponseVO responseVO = (ResponseVO)body;
            return responseVO;
        }
        return ResponseVO.SUCCESS(body);
    }


    @ExceptionHandler(value = {Exception.class})
    public final ResponseVO exceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception e) {
        return this.handleException(e, request);
    }

    protected ResponseVO handleException(Exception exception, HttpServletRequest request) {

        StringBuilder message = new StringBuilder();
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        message.append("未知异常: ");
        message.append(sw.toString());
        String code = "500";
        log.error("异常堆栈: ", exception);
        return ResponseVO.FAIL(code, message.toString());
    }
}

