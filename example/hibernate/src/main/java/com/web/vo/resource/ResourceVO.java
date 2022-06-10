package com.web.vo.resource;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.web.model.QResourceDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class ResourceVO {

    @ApiModelProperty(value = "资源id")
    private String id;

    @ApiModelProperty(value = "资源名")
    private String resourceName;

    @ApiModelProperty(value = "资源请求路径")
    private String url;

    @ApiModelProperty(value = "方法名")
    private String methodName;

    @ApiModelProperty(value = "资源组名")
    private String resourceGroupName;

    public Predicate buildResource() {
        BooleanBuilder builder = new BooleanBuilder();
        QResourceDO qResourceDO = QResourceDO.resourceDO;
        if (!StringUtils.isEmpty(this.resourceName)) {
            builder.and(qResourceDO.resourceName.like(this.resourceName));
        }
        if (!StringUtils.isEmpty(this.url)) {
            builder.and(qResourceDO.url.eq(this.url));
        }
        if (!StringUtils.isEmpty(this.methodName)) {
            builder.and(qResourceDO.methodName.eq(this.methodName));
        }
        if (!StringUtils.isEmpty(this.resourceGroupName)) {
            builder.and(qResourceDO.methodName.eq(this.resourceGroupName));
        }
        return builder;
    }
}
