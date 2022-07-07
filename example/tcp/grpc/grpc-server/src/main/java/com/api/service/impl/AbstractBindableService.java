package com.api.service.impl;

import io.grpc.BindableService;
import io.grpc.ServerServiceDefinition;

/**
 * @ Author     : wuhp
 * @ Date       : 2022/1/14
 * @ Description: 描述
 */
public class AbstractBindableService implements BindableService {

    @Override
    public ServerServiceDefinition bindService() {
        return ServerServiceDefinition.builder(this.getClass().getName()).build();
    }
}
