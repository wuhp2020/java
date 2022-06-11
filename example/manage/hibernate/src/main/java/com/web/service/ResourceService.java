package com.web.service;

import com.google.common.collect.Lists;
import com.querydsl.core.types.Predicate;
import com.web.model.ResourceDO;
import com.web.model.RoleDO;
import com.web.model.RoleLinkedResourceDO;
import com.web.model.UserDO;
import com.web.model.UserLinkedRoleDO;
import com.web.repository.ResourceRepository;
import com.web.repository.RoleLinkedResourceRepository;
import com.web.repository.RoleRepository;
import com.web.repository.UserLinkedRoleRepository;
import com.web.repository.UserRepository;
import com.web.vo.resource.ResourceVO;
import com.web.vo.role.RoleVO;
import com.web.vo.user.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;

    @Value("${server.port}")
    private String port;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleLinkedResourceRepository roleLinkedResourceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserLinkedRoleRepository userLinkedRoleRepository;


    /**
     * 查询资源树
     * @return
     * @throws Exception
     */
    public List<ResourceVO> findResource() throws Exception{
        List<ResourceDO> resourceDOs = (List<ResourceDO>)resourceRepository.findAll();
        List<ResourceVO> resourceVOs = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(resourceDOs)) {
            resourceDOs.stream().forEach(resourceDO -> {
                ResourceVO resourceVO = new ResourceVO();
                BeanUtils.copyProperties(resourceDO, resourceVO);
                resourceVOs.add(resourceVO);
            });
        }
        return resourceVOs;
    }

    /**
     * 资源整理
     * @throws Exception
     */
    @Transactional
    public void initResource() {
        try {
            List<ResourceDO> resourceOlds = (List<ResourceDO>)resourceRepository.findAll();
            resourceRepository.deleteAll();

            // 获取swagger内容
            String url = "http://127.0.0.1:"+ port +"/v2/api-docs";
            RestTemplate restTemplate = new RestTemplate();
            Map swaggerMap = restTemplate.getForObject(url, Map.class);

            // 组织ResourceDO
            Map resources = (Map)swaggerMap.get("paths");
            List<ResourceDO> resourceNews = Lists.newArrayList();
            if (!CollectionUtils.isEmpty(resources)) {
                for (String resourceURL : (Set<String>)resources.keySet()) {
                    ResourceDO ResourceDO = new ResourceDO();
                    // 资源路径
                    ResourceDO.setUrl(resourceURL);
                    Map type = (Map)resources.get(resourceURL);

                    for (String requestType : (Set<String>) type.keySet()) {
                        Map labelMap = (Map)type.get(requestType);
                        //资源组名
                        List<String> resourceGroupNames = (List<String>) labelMap.get("tags");
                        if (!CollectionUtils.isEmpty(resourceGroupNames)) {
                            String resourceGroupName = resourceGroupNames.get(0);
                            ResourceDO.setResourceGroupName(resourceGroupName);
                        }

                        //资源名
                        String resourceName = (String)labelMap.get("summary");
                        ResourceDO.setResourceName(resourceName);
                    }
                    resourceNews.add(ResourceDO);
                }
            }

            resourceRepository.saveAll(resourceNews);

            this.initRoleResource(resourceOlds);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化角色资源
     * @param resourceOlds
     */
    @Transactional
    public void initRoleResource(List<ResourceDO> resourceOlds) {
        try {
            List<ResourceDO> resourceNews = (List<ResourceDO>)resourceRepository.findAll();
            List<RoleDO> roleOlds = (List<RoleDO>)roleRepository.findAll();
            roleLinkedResourceRepository.deleteAll();

            if(!CollectionUtils.isEmpty(roleOlds)
                    && !CollectionUtils.isEmpty(resourceOlds) && !CollectionUtils.isEmpty(resourceNews)) {
                Map<String, List<ResourceDO>> resourceNewMap =
                        resourceNews.stream().collect(Collectors.groupingBy(ResourceDO::getUrl));

                Map<String, List<ResourceDO>> resourceOldMap =
                        resourceOlds.stream().collect(Collectors.groupingBy(ResourceDO::getId));

                for(RoleDO roleDO: roleOlds) {
                    if ("超级管理员".equals(roleDO.getRoleName())) {
                        roleDO.getRoleLinkedResources().clear();
                        continue;
                    }
                    List<RoleLinkedResourceDO> roleLinkedResourceOlds = roleDO.getRoleLinkedResources();
                    List<RoleLinkedResourceDO> roleLinkedResourceNews = Lists.newArrayList();
                    if (!CollectionUtils.isEmpty(roleLinkedResourceOlds)) {
                        roleLinkedResourceOlds.stream()
                                .map(o -> o.getResourceId() +"-"+ o.getRole().getId())
                                .distinct().forEach(o -> {
                                    String[] s = o.split("-");
                                    String oldUrl = resourceOldMap.get(s[0]).get(0).getUrl();
                                    List<ResourceDO> resourceNewList = resourceNewMap.get(oldUrl);
                                    if (!CollectionUtils.isEmpty(resourceNewList)) {
                                        ResourceDO resourceNew = resourceNewList.get(0);
                                        RoleLinkedResourceDO roleLinkedResourceDO = new RoleLinkedResourceDO();
                                        roleLinkedResourceDO.setResourceId(resourceNew.getId());
                                        roleLinkedResourceDO.setRole(roleDO);
                                        roleLinkedResourceNews.add(roleLinkedResourceDO);
                                    }
                                });
                    }
                    roleDO.setRoleLinkedResources(roleLinkedResourceNews);
                }
                roleRepository.saveAll(roleOlds);
            }

            this.initAdminUserRole();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 初始用户/角色
     * @return
     */
    @Transactional
    private void initAdminUserRole() {
        // 初始化一个角色
        RoleVO roleVO = new RoleVO();
        roleVO.setRoleName("超级管理员");
        Predicate predicate = roleVO.buildRole();

        List<RoleDO> roleDOS = (List<RoleDO>)roleRepository.findAll(predicate);
        RoleDO roleDO = new RoleDO();
        if (CollectionUtils.isEmpty(roleDOS)) {
            BeanUtils.copyProperties(roleVO, roleDO);
        } else {
            roleDO = roleDOS.get(0);
        }

        List<ResourceDO> resources = (List<ResourceDO>) resourceRepository.findAll();
        if (!CollectionUtils.isEmpty(resources)) {
            List<RoleLinkedResourceDO> roleLinkedResources = Lists.newArrayList();
            for (ResourceDO resource : resources) {
                RoleLinkedResourceDO roleLinkedResource = new RoleLinkedResourceDO();
                roleLinkedResource.setRole(roleDO);
                roleLinkedResource.setResourceId(resource.getId());
                roleLinkedResources.add(roleLinkedResource);
            }
            roleDO.setRoleLinkedResources(roleLinkedResources);
        }
        roleDO = roleRepository.save(roleDO);

        UserDO userDO = new UserDO();
        UserVO userVO = new UserVO();
        userVO.setUsername("admin");
        Predicate userPredicate = userVO.buildUser();
        List<UserDO> userDOS = (List<UserDO>)userRepository.findAll(userPredicate);

        if (CollectionUtils.isEmpty(userDOS)) {
            userDO.setPassword(new BCryptPasswordEncoder().encode("888888"));
            userDO.setUsername(userVO.getUsername());
        } else {
            userDO = userDOS.get(0);
        }

        List<UserLinkedRoleDO> userLinkedRolesTemp = userDO.getUserLinkedRoles();
        if (!CollectionUtils.isEmpty(userLinkedRolesTemp)) {
            userLinkedRoleRepository.deleteAll(userLinkedRolesTemp);
        }

        //初始化admin用户的角色
        List<UserLinkedRoleDO> userLinkedRoles = Lists.newLinkedList();
        UserLinkedRoleDO userLinkedRoleDO = new UserLinkedRoleDO();
        userLinkedRoleDO.setRoleId(roleDO.getId());
        userLinkedRoleDO.setUser(userDO);
        userLinkedRoles.add(userLinkedRoleDO);
        userDO.setUserLinkedRoles(userLinkedRoles);

        userRepository.save(userDO);
    }
}
