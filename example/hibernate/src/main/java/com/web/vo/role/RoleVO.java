package com.web.vo.role;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.web.model.QRoleDO;
import com.web.vo.resource.ResourceVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.List;

@Data
public class RoleVO {

    @ApiModelProperty(value = "角色id")
    private String id;

    @ApiModelProperty(value = "角色名")
    private String roleName;

    @ApiModelProperty(value = "资源")
    private List<ResourceVO> resourceVOs;

    public Predicate buildRole() {
        BooleanBuilder builder = new BooleanBuilder();
        QRoleDO qRoleDO = QRoleDO.roleDO;
        if (!StringUtils.isEmpty(this.roleName)) {
            builder.and(qRoleDO.roleName.like(this.roleName));
        }
        return builder;
    }
}
