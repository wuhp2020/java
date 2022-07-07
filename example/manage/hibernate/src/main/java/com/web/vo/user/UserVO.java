package com.web.vo.user;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.web.model.QUserDO;
import com.web.vo.role.RoleVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.List;

@Data
public class UserVO {

    @ApiModelProperty(value = "用户id")
    private String id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "用户密码")
    private String password;

    @ApiModelProperty(value = "拥有角色")
    private List<RoleVO> roleVOs;

    public Predicate buildUser() {
        BooleanBuilder builder = new BooleanBuilder();
        QUserDO qUserDO = QUserDO.userDO;
        if (!StringUtils.isEmpty(this.username)) {
            builder.and(qUserDO.username.eq(this.username));
        }
        return builder;
    }
}
