package com.web.vo.operationlog;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.web.model.QOperationLogDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.Date;

@Data
public class OperationLogVO {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "客户端IP")
    private String clientIP;

    @ApiModelProperty(value = "请求时间")
    private Date requestTime;

    @ApiModelProperty(value = "请求路径")
    private String requestURL;

    @ApiModelProperty(value = "请求用户")
    private String username;

    @ApiModelProperty(value = "入参")
    private String inputParams;

    @ApiModelProperty(value = "出参")
    private String outputParams;

    public Predicate buildOperationLog() {
        BooleanBuilder builder = new BooleanBuilder();
        QOperationLogDO qOperationLogDO = QOperationLogDO.operationLogDO;
        if (!StringUtils.isEmpty(this.clientIP)) {
            builder.and(qOperationLogDO.clientIP.eq(this.clientIP));
        }
        if (!StringUtils.isEmpty(this.requestURL)) {
            builder.and(qOperationLogDO.requestURL.eq(this.requestURL));
        }
        if (!StringUtils.isEmpty(this.username)) {
            builder.and(qOperationLogDO.username.eq(this.username));
        }
        return builder;
    }
}
