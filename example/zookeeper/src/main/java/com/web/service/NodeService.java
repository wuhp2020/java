package com.web.service;

import com.web.vo.node.NodeQueryVO;
import com.web.vo.node.NodeVO;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class NodeService {

    @Autowired
    private ZkClient zkClient;

    public void addPersistent(NodeVO nodeVO) throws Exception {

    }

    public void addPersistentSequential(NodeVO nodeVO) throws Exception {

    }

    public void addEphemeral(NodeVO nodeVO) throws Exception {

    }

    public void addEphemeralSequential(NodeVO nodeVO) throws Exception {

    }

    public List<String> findOne(NodeQueryVO nodeQueryVO) throws Exception {
        return zkClient.getChildren(nodeQueryVO.getPath());
    }

}
