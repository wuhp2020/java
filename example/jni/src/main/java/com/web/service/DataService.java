package com.web.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DataService {

    public String search() throws Exception {
        String osName = System.getProperty("os.name");
        if (osName.startsWith("Linux")) {
            SearchJNI.searchByLinux();
        } else if (osName.startsWith("Mac")) {
            SearchJNI.searchByMac();
        } else if (osName.startsWith("Windows")) {
            SearchJNI.searchByWindows();
        }
        return "exec ok";
    }
}
