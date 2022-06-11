package com.web.vo.user;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.web.model.QUserDO;
import com.web.vo.common.PageQueryVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class UserQueryVO extends PageQueryVO {

    @ApiModelProperty(value = "用户名")
    private String username;

    public Predicate buildUser() {
        BooleanBuilder builder = new BooleanBuilder();
        QUserDO qUserDO = QUserDO.userDO;
        if (!StringUtils.isEmpty(this.username)) {
            builder.and(qUserDO.username.like(this.username));
        }
        return builder;
    }
}
