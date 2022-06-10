package com.web.vo.operationlog;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.web.model.QOperationLogDO;
import com.web.vo.common.PageQueryVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.Date;

@Data
public class OperationLogQueryVO extends PageQueryVO {

    @ApiModelProperty(value = "请求ip")
    private String clientIP;

    @ApiModelProperty(value = "请求时间段开始")
    private Date requestBeginTime;

    @ApiModelProperty(value = "请求时间段结束")
    private Date requestEndTime;

    @ApiModelProperty(value = "请求路径")
    private String requestURL;

    @ApiModelProperty(value = "用户名")
    private String username;

    public Predicate buildOperationLog() {
        BooleanBuilder builder = new BooleanBuilder();
        QOperationLogDO qOperationLogDO = QOperationLogDO.operationLogDO;
        if (!StringUtils.isEmpty(this.clientIP)) {
            builder.and(qOperationLogDO.clientIP.eq(this.clientIP));
        }
        if (this.requestBeginTime != null) {
            builder.and(qOperationLogDO.requestTime.gt(this.requestBeginTime));
        }
        if (this.requestEndTime != null) {
            builder.and(qOperationLogDO.requestTime.lt(this.requestEndTime));
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
