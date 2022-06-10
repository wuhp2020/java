package com.web.service;

import java.util.ServiceLoader;

/**
 * @ Author : wuheping
 * @ Date   : 2022/5/7
 * @ Desc   : 描述
 */
public class SIPLoaderService {

    public static void main(String[] args) {
        ServiceLoader<SIPService> serviceLoader = ServiceLoader.load(SIPService.class);
        for (SIPService sipService :serviceLoader) {
            System.out.println(sipService.getName());
        }
    }
}
