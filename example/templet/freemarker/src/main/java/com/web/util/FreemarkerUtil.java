package com.web.util;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.StringWriter;

public class FreemarkerUtil {

    public static String xml(String path, Object dataModel) throws Exception {
        Configuration cfg = new Configuration(Configuration.getVersion());
        TemplateLoader templateLoader = new ClassTemplateLoader(FreemarkerUtil.class.getClass(), "templates");
        cfg.setTemplateLoader(templateLoader);
        Template template = cfg.getTemplate(path, "UTF-8");
        StringWriter sw = new StringWriter();
        template.process(dataModel, sw);
        return sw.toString();
    }
}
