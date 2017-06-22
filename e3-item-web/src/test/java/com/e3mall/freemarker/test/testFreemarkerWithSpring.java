package com.e3mall.freemarker.test;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;

/**
 * Created by Suny on 2017/6/22.
 */
public class testFreemarkerWithSpring {

    @Test
    public void test() throws IOException, TemplateException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring/springmvc.xml");
        FreeMarkerConfigurer bean = context.getBean(FreeMarkerConfigurer.class);
        Configuration configuration = bean.getConfiguration();
        Template template = configuration.getTemplate("hello.ftl");
        HashMap<Object, Object> map = new HashMap<>();
        map.put("hello","aaaaaaa");
        Writer writer = new FileWriter(new File("D:\\temp\\freemarker\\testspring.html"));
        template.process(map,writer);
        writer.close();
    }
}
