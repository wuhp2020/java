package com.web.vo.role;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.web.model.QRoleDO;
import com.web.vo.common.PageQueryVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class RoleQueryVO extends PageQueryVO {

    @ApiModelProperty(value = "角色名")
    private String roleName;

    public Predicate buildRole() {
        BooleanBuilder builder = new BooleanBuilder();
        QRoleDO qRoleDO = QRoleDO.roleDO;
        if (!StringUtils.isEmpty(this.roleName)) {
            builder.and(qRoleDO.roleName.like(this.roleName));
        }
        return builder;
    }
}
