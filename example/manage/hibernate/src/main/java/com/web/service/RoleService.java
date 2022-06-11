package com.web.service;

import com.google.common.collect.Lists;
import com.querydsl.core.types.Predicate;
import com.web.model.ResourceDO;
import com.web.model.RoleDO;
import com.web.model.RoleLinkedResourceDO;
import com.web.repository.ResourceRepository;
import com.web.repository.RoleLinkedResourceRepository;
import com.web.repository.RoleRepository;
import com.web.vo.resource.ResourceVO;
import com.web.vo.role.RoleQueryVO;
import com.web.vo.role.RoleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private RoleLinkedResourceRepository roleLinkedResourceRepository;


    /**
     * 增
     * @param roleVO
     * @return
     * @throws Exception
     */
    @Transactional
    public RoleVO save(RoleVO roleVO) throws Exception{
        RoleDO roleDO = new RoleDO();
        BeanUtils.copyProperties(roleVO, roleDO);
        roleDO = roleRepository.save(roleDO);
        List<RoleLinkedResourceDO> roleLinkedResourceDOs = Lists.newArrayList();

        List<ResourceVO> resourceVOs = roleVO.getResourceVOs();
        if (!CollectionUtils.isEmpty(resourceVOs)) {
            for (ResourceVO resourceVO : resourceVOs) {
                RoleLinkedResourceDO roleLinkedResourceDO = new RoleLinkedResourceDO();
                roleLinkedResourceDO.setRole(roleDO);
                roleLinkedResourceDO.setResourceId(resourceVO.getId());
                roleLinkedResourceDOs.add(roleLinkedResourceDO);
            }
        }
        roleLinkedResourceRepository.saveAll(roleLinkedResourceDOs);

        roleVO.setId(roleDO.getId());
        return roleVO;
    }

    public Pair<Boolean, String> checkRepeat(RoleVO roleVO) throws Exception{
        List<RoleDO> roles = this.findByRoleName(roleVO);
        if (!CollectionUtils.isEmpty(roles)) {
            return Pair.of(false, "角色名: "+ roleVO.getRoleName() + " 已经存在");
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
        ids.stream().forEach(id -> roleRepository.deleteById(id));
        return Pair.of(true, "OK");
    }

    public Pair<Boolean, String> roleIdNull(RoleVO roleVO) {
        if (roleVO != null && !StringUtils.isEmpty(roleVO.getId())) {
            return Pair.of(true, "OK");
        }
        return Pair.of(false, "id为空");
    }

    /**
     * 改
     * @param roleVO
     * @return
     * @throws Exception
     */
    @Transactional
    public RoleVO update(RoleVO roleVO) throws Exception{
        RoleDO roleDO = roleRepository.findById(roleVO.getId()).get();
        roleDO.getRoleLinkedResources().stream().forEach(roleLinkedResourceDO -> roleLinkedResourceRepository.delete(roleLinkedResourceDO));
        roleDO.setRoleName(roleVO.getRoleName());
        List<RoleLinkedResourceDO> roleLinkedResourceDOs = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(roleVO.getResourceVOs())) {
            for (ResourceVO resourceVO: roleVO.getResourceVOs()){
                RoleLinkedResourceDO roleLinkedResourceDO = new RoleLinkedResourceDO();
                roleLinkedResourceDO.setRole(roleDO);
                roleLinkedResourceDO.setResourceId(resourceVO.getId());
                roleLinkedResourceDOs.add(roleLinkedResourceDO);
            }
        }
        roleDO.setRoleLinkedResources(roleLinkedResourceDOs);
        roleRepository.save(roleDO);
        return roleVO;
    }

    public List<RoleDO> findByRoleName(RoleVO roleVO) throws Exception{
        Predicate predicate = roleVO.buildRole();
        List<RoleDO> roleDOs = (List<RoleDO>)roleRepository.findAll(predicate);
        return roleDOs;
    }

    /**
     * 单个查询
     * @param id
     * @return
     * @throws Exception
     */
    public RoleVO findOne(String id) throws Exception{
        RoleDO roleDO = roleRepository.findById(id).get();
        List<ResourceDO> resourceDOs = (List<ResourceDO>)resourceRepository.findAll();
        RoleVO roleVO = new RoleVO();
        BeanUtils.copyProperties(roleDO, roleVO);

        // 关联的资源
        List<ResourceVO> resourceVOs = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(roleDO.getRoleLinkedResources())) {
            for (RoleLinkedResourceDO roleLinkedResourceDO: roleDO.getRoleLinkedResources()) {
                ResourceVO resourceVO = new ResourceVO();
                resourceVO.setId(roleLinkedResourceDO.getResourceId());
                resourceVOs.add(resourceVO);
            }
        }
        roleVO.setResourceVOs(resourceVOs);
        return roleVO;
    }

    /**
     * 分页查询
     * @param roleQueryVO
     * @return
     * @throws Exception
     */
    public Page<RoleVO> findByPage(RoleQueryVO roleQueryVO) throws Exception{
        Pageable pageable = PageRequest.of(roleQueryVO.getPageNum(),
                roleQueryVO.getPageSize(), Sort.Direction.DESC, "id");
        Predicate predicate = roleQueryVO.buildRole();
        Page<RoleDO> pageDOs = roleRepository.findAll(predicate, pageable);
        List<RoleVO> roleVOs = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(pageDOs.getContent())) {
            List<RoleDO> roleDOs = pageDOs.getContent();
            roleDOs.stream().forEach(roleDOResult -> {
                RoleVO roleVO = new RoleVO();
                BeanUtils.copyProperties(roleDOResult, roleVO);
                roleVOs.add(roleVO);
            });
        }
        return new PageImpl<RoleVO>(roleVOs, pageable, pageDOs.getTotalElements());
    }

}
