package com.web.service;

import com.web.vo.role.PermissionVO;
import com.web.vo.role.RoleVO;
import com.web.vo.user.UserVO;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class LoginService {

    public UserVO getUserByName(String getMapByName) {
        return getMapByName(getMapByName);
    }

    /**
     * 模拟数据库查询
     * @param userName
     * @return
     */
    private UserVO getMapByName(String userName) {
        PermissionVO permission1 = new PermissionVO("1", "query");
        PermissionVO permission2 = new PermissionVO("2", "add");
        Set<PermissionVO> permissionsSet = new HashSet<>();
        permissionsSet.add(permission1);
        permissionsSet.add(permission2);
        RoleVO role = new RoleVO("1", "admin", permissionsSet);
        Set<RoleVO> roleSet = new HashSet<>();
        roleSet.add(role);
        UserVO user = new UserVO("1", "wsl", "123456", roleSet);
        Map<String, UserVO> map = new HashMap<>();
        map.put(user.getUserName(), user);

        Set<PermissionVO> permissionsSet1 = new HashSet<>();
        permissionsSet1.add(permission1);
        RoleVO role1 = new RoleVO("2", "user", permissionsSet1);
        Set<RoleVO> roleSet1 = new HashSet<>();
        roleSet1.add(role1);
        UserVO user1 = new UserVO("2", "zhangsan", "123456", roleSet1);
        map.put(user1.getUserName(), user1);
        return map.get(userName);
    }
}
