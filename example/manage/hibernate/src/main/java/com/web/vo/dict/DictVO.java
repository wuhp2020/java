package com.web.vo.dict;

import com.google.common.collect.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.*;
import com.web.model.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.util.*;


@Data
public class DictVO {
    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "编码")
    private String code;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "中文名")
    private String name;

    @ApiModelProperty(value = "关联")
    private String relation;

    public Predicate buildDict() {
        BooleanBuilder builder = new BooleanBuilder();
        QDictDO qDictDO = QDictDO.dictDO;
        if (!StringUtils.isEmpty(this.code)) {
            builder.and(qDictDO.code.eq(this.code));
        }
        if (!StringUtils.isEmpty(this.type)) {
            builder.and(qDictDO.type.eq(this.type));
        }
        if (!StringUtils.isEmpty(this.name)) {
            builder.and(qDictDO.name.like(this.name));
        }
        return builder;
    }
}
