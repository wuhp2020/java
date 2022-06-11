package com.web.service;

import com.google.common.collect.Lists;
import com.querydsl.core.types.Predicate;
import com.web.model.UserDO;
import com.web.model.UserLinkedRoleDO;
import com.web.repository.UserLinkedRoleRepository;
import com.web.repository.UserRepository;
import com.web.vo.role.RoleVO;
import com.web.vo.user.UserQueryVO;
import com.web.vo.user.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.util.Pair;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserLinkedRoleRepository userLinkedRoleRepository;

    /**
     * 增
     * @param userVO
     * @return
     * @throws Exception
     */
    public UserVO save(UserVO userVO) throws Exception{
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userVO, userDO);
        userDO.setPassword(new BCryptPasswordEncoder().encode(userVO.getPassword()));
        userDO = userRepository.save(userDO);
        List<UserLinkedRoleDO> userLinkedRoleDOs = Lists.newArrayList();
        List<RoleVO> roleVOs = userVO.getRoleVOs();
        if(!CollectionUtils.isEmpty(roleVOs)){
            for (RoleVO roleVO: roleVOs){
                UserLinkedRoleDO userLinkedRoleDO = new UserLinkedRoleDO();
                userLinkedRoleDO.setRoleId(roleVO.getId());
                userLinkedRoleDO.setUser(userDO);
                userLinkedRoleDOs.add(userLinkedRoleDO);
            }
        }

        userLinkedRoleRepository.saveAll(userLinkedRoleDOs);
        userVO.setId(userDO.getId());
        return userVO;
    }

    public Pair<Boolean, String> checkRepeat(UserVO userVO) throws Exception{
        List<UserDO> users = this.findByUserName(userVO);
        if (!CollectionUtils.isEmpty(users)) {
            return Pair.of(false, "用户名: "+ userVO.getUsername() + " 已经存在");
        }
        return Pair.of(true, "OK");
    }

    /**
     * 删
     * @param ids
     * @return
     */
    @Transactional
    public Pair<Boolean, String> delete(List<String> ids) {
        ids.stream().forEach(id -> userRepository.deleteById(id));
        return Pair.of(true, "OK");
    }

    /**
     * 改
     * @param userVO
     * @return
     * @throws Exception
     */
    @Transactional
    public UserVO update(UserVO userVO) throws Exception{
        UserDO userDO = userRepository.findById(userVO.getId()).get();
        userDO.getUserLinkedRoles().stream().forEach(userLinkedRoleDO -> userLinkedRoleRepository.delete(userLinkedRoleDO));
        userDO.setUsername(userVO.getUsername());
        userDO.setPassword(new BCryptPasswordEncoder().encode(userVO.getPassword()));
        List<UserLinkedRoleDO> userLinkedRoleDOs = Lists.newArrayList();
        List<RoleVO> roleVOs = userVO.getRoleVOs();
        if(!CollectionUtils.isEmpty(roleVOs)){
            for(RoleVO roleVO: roleVOs) {
                UserLinkedRoleDO userLinkedRoleDO = new UserLinkedRoleDO();
                userLinkedRoleDO.setRoleId(roleVO.getId());
                userLinkedRoleDO.setUser(userDO);
                userLinkedRoleDOs.add(userLinkedRoleDO);
            }
        }
        userDO.setUserLinkedRoles(userLinkedRoleDOs);
        userRepository.save(userDO);
        return userVO;
    }

    /**
     * 单个查询
     * @param id
     * @return
     * @throws Exception
     */
    public UserVO findOne(String id) throws Exception{
        UserDO userDO = userRepository.findById(id).get();
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userDO, userVO);
        // 关联的角色
        List<RoleVO> roleVOs = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(userDO.getUserLinkedRoles())) {
            for (UserLinkedRoleDO userLinkedRoleDO: userDO.getUserLinkedRoles()) {
                RoleVO roleVO = new RoleVO();
                roleVO.setId(userLinkedRoleDO.getRoleId());
                roleVOs.add(roleVO);
            }
        }
        userVO.setRoleVOs(roleVOs);
        return userVO;
    }

    public List<UserDO> findByUserName(UserVO userVO) throws Exception{
        Predicate predicate = userVO.buildUser();
        List<UserDO> userDOs = (List<UserDO>)userRepository.findAll(predicate);
        return userDOs;
    }

    /**
     * 分页查询
     * @param userQueryVO
     * @return
     * @throws Exception
     */
    public Page<UserVO> findByPage(UserQueryVO userQueryVO) throws Exception{
        Pageable pageable = PageRequest.of(userQueryVO.getPageNum(),
                userQueryVO.getPageSize(), Sort.Direction.DESC, "id");
        Predicate predicate = userQueryVO.buildUser();
        Page<UserDO> pageDOs = userRepository.findAll(predicate, pageable);
        List<UserVO> userVOs = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(pageDOs.getContent())) {
            List<UserDO> userDOs = pageDOs.getContent();
            userDOs.stream().forEach(userDOResult -> {
                UserVO userVO = new UserVO();
                BeanUtils.copyProperties(userDOResult, userVO);
                userVOs.add(userVO);
            });
        }
        return new PageImpl<UserVO>(userVOs, pageable, pageDOs.getTotalElements());
    }
}
