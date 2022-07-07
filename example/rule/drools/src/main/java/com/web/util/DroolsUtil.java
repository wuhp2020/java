package com.web.util;

import com.spring.config.DroolsConfig;
import org.drools.decisiontable.InputType;
import org.drools.decisiontable.SpreadsheetCompiler;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.utils.KieHelper;
import org.springframework.util.StringUtils;

import java.io.*;

/**
 * @ Author     : wuhp
 * @ Date       : 2022/1/19
 * @ Description: 描述
 */
public class DroolsUtil {

    public void addKieSession(String fileName, String drl) throws Exception {
        if (!StringUtils.isEmpty(drl)) {
            KieHelper helper = new KieHelper();
            helper.addContent(drl, ResourceType.DRL);
            KieSession kieSession = helper.build().newKieSession();
            DroolsConfig.kieSessionMap.put(fileName, kieSession);
        } else {
            throw new Exception("文件内容为空");
        }
    }

    public String xlsx2drl(File file) throws Exception {
        String drl;
        if (file.getName().endsWith(".xls") || file.getName().endsWith(".xlsx")) {

            InputStream is = new FileInputStream(file);
            SpreadsheetCompiler converter = new SpreadsheetCompiler();
            drl = converter.compile(is, InputType.XLS);
        } else {
            throw new Exception("不是.xls或.xlsx结尾");
        }
        return drl;
    }


}
