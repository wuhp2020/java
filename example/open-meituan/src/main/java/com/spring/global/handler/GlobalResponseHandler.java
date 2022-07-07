package com.spring.global.handler;

import com.google.gson.Gson;
import com.web.vo.common.ResponseVO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.List;


@Slf4j
@RestControllerAdvice
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

    @Value("${swagger.urls}")
    private List<String> swaggerUrls;

    private Gson gson = new Gson();

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
        log.info("=====>>>>> 返回报文: {}", gson.toJson(body));
        return body;
    }


    @ExceptionHandler(value = {Exception.class})
    public final ResponseVO exceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception e) {
        return this.handleException(e, request);
    }

    protected ResponseVO handleException(Exception exception, HttpServletRequest request) {

        String message = "成功";
        String code = "200";
        if (exception instanceof ClassCastException) {
            message = "类型转换异常";
            code = "501";
        } else if (exception instanceof NullPointerException) {
            message = "空指针异常";
            code = "502";
        } else if (exception instanceof ArrayIndexOutOfBoundsException) {
            message = "数组越界异常";
            code = "503";
        } else if (exception instanceof Exception) {
            message = "未知异常: " + exception.getMessage();
            code = "505";
        }
        log.error("异常堆栈: ", exception);
        return ResponseVO.FAIL(code, message);
    }
}

