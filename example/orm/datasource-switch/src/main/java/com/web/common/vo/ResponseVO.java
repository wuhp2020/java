package com.web.common.vo;

import lombok.Data;

@Data
public class ResponseVO<T> {

    private String code;
    private String message;
    private T data;

    public ResponseVO(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ResponseVO SUCCESS(T data) {
        return new ResponseVO<T>("200", "成功", data);
    }

    public static ResponseVO FAIL(String message) {
        return new ResponseVO("500", message, "");
    }

}
