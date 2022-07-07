package com.web.service;

import com.ecwid.consul.v1.ConsulClient;
import com.web.vo.node.NodeQueryVO;
import com.web.vo.node.ConfigVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class NodeService {

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private ConsulClient consulClient;

    public void add(ConfigVO configVO) throws Exception {
        consulClient.setKVValue(configVO.getKey(), configVO.getValue());
    }

    public List<ServiceInstance> findOne(NodeQueryVO nodeQueryVO) throws Exception {
        List<ServiceInstance> instances = discoveryClient.getInstances(nodeQueryVO.getServiceName());
        return instances;
    }
}
