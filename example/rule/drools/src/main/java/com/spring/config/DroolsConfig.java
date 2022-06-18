package com.spring.config;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.drools.decisiontable.InputType;
import org.drools.decisiontable.SpreadsheetCompiler;
import org.kie.api.KieServices;
import org.kie.api.builder.*;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.utils.KieHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

@Configuration
@Slf4j
public class DroolsConfig {

    public static final String RULES_PATH = "rule/";
    public static final Map<String, KieSession> kieSessionMap = Maps.newConcurrentMap();

    @Bean(name = "kieSessionMap")
    public Map<String, KieSession> kieSessionMap() throws Exception {
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        Resource[] files = resourcePatternResolver.getResources("classpath*:" + RULES_PATH + "*.*");
        for (Resource file : files) {
            if (file.getFilename().endsWith(".xls") || file.getFilename().endsWith(".xlsx")) {
                String path = RULES_PATH + file.getFilename();
                log.info("excel path : " + path);

                InputStream is = new FileInputStream(file.getFile());
                SpreadsheetCompiler converter = new SpreadsheetCompiler();
                String drl = converter.compile(is, InputType.XLS);

                KieHelper helper = new KieHelper();
                helper.addContent(drl, ResourceType.DRL);
                KieSession kieSession = helper.build().newKieSession();
                kieSessionMap.put(file.getFilename(), kieSession);
            } else if (file.getFilename().endsWith(".drl")) {
                KieServices kieServices = KieServices.Factory.get();
                KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
                String path = RULES_PATH + file.getFilename();
                log.info("drl path : " + path);
                kieFileSystem.write(ResourceFactory.newClassPathResource(path, "UTF-8"));
                final KieRepository kieRepository = kieServices.getRepository();
                kieRepository.addKieModule(new KieModule() {
                    @Override
                    public ReleaseId getReleaseId() {
                        return kieRepository.getDefaultReleaseId();
                    }
                });
                KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
                kieBuilder.buildAll();
                KieContainer kieContainer = kieServices.newKieContainer(kieRepository.getDefaultReleaseId());
                KieSession kieSession = kieContainer.newKieSession();
                kieSessionMap.put(file.getFilename(), kieSession);
            }
        }
        return kieSessionMap;
    }
}
