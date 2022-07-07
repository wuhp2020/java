package com.spring.config;

import com.web.service.UserService;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;

@Configuration
public class CXFConfig {

    @Autowired
    private Bus bus;

    @Autowired
    private UserService userService;

    @Bean
    public Endpoint userEndpoint() {
        EndpointImpl endpoint = new EndpointImpl(bus,userService);
        endpoint.publish("/user");
        return endpoint;
    }

}
