package com.web.vo.dict;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.web.model.QDictDO;
import com.web.vo.common.PageQueryVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class DictQueryVO extends PageQueryVO {

    @ApiModelProperty(value = "编码")
    private String code;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "中文名")
    private String name;

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
