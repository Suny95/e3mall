package com.e3mall.freemarker.test;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;


/**
 * Created by Suny on 2017/6/22.
 */
public class TestFreemarker {

    @Test
    public void test1() throws IOException, TemplateException {
        //创建一个模板文件
        //创建一个configuration对象
        Configuration configuration = new Configuration(Configuration.getVersion());
        //设置模板所在的目录
        configuration.setDirectoryForTemplateLoading(new File("D:/workspaces/IDEA/e3parent/e3-item-web/src/main/webapp/WEB-INF/ftl"));
        //模板的编码格式
        configuration.setDefaultEncoding("utf-8");
        //创建模板对象,加载模板文件
        Template template = configuration.getTemplate("hello.ftl");
        //创建数据集, 可以是POJO也可以是Map,key对应模板文件中取值的key
        Map map = new HashMap();
        map.put("hello","hahahahaha");
        //创建Writer对象,指定输出文件的位置及文件名
        Writer out = new FileWriter(new File("D:/temp/freemarker/hello.html"));
        //生成静态页面
        template.process(map,out);
        //关闭流
        out.close();
    }

    @Test
    public void test2() throws IOException, TemplateException {
        //创建configuration对象
        Configuration configuration = new Configuration(Configuration.getVersion());
        //设置模板所在目录
        configuration.setDirectoryForTemplateLoading(new File("D:/workspaces/IDEA/e3parent/e3-item-web/src/main/webapp/WEB-INF/ftl"));
        //设置模板的编码
        configuration.setDefaultEncoding("utf-8");
        //创建模板对象,加载模板文件
        Template template = configuration.getTemplate("student.ftl");
        //创建数据集
        Map map = new HashMap<>();
        //创建集合
        List<Student> list = new ArrayList<>();
        list.add(new Student(1,"张三1",24,"上海"));
        list.add(new Student(2,"张三2",25,"上海"));
        list.add(new Student(3,"张三3",26,"上海"));
        list.add(new Student(4,"张三4",27,"上海"));
        list.add(new Student(5,"张三5",28,"上海"));
        map.put("student",new Student(0,"张三",23,"北京"));
        map.put("stuList",list);

        map.put("date",new Date());
        map.put("val","1111111");

        map.put("hello","hahahahaha");
        //创建writer对象,指定输出目录与文件名
        Writer out = new FileWriter(new File("D:/temp/freemarker/student.html"));
        //生成页面
        template.process(map,out);
        //关闭流
        out.close();
    }
}
