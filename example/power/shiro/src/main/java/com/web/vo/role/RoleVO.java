package com.web.vo.role;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class RoleVO {
    private String id;
    private String roleName;
    /**
     * 角色对应权限集合
     */
    private Set<PermissionVO> permissions;
}
