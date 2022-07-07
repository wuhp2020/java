package com.web.service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BIOSingleThreadTrainThreadPoolLongLinkClient {

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 10; i++) {
            new Thread(new BIOThreadPoolLongLinkRequest(i)).start();
        }
    }
}
