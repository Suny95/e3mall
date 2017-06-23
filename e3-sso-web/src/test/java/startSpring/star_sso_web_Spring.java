package startSpring;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * Created by Suny on 2017/6/23.
 */
public class star_sso_web_Spring {

    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring/*.xml");
        System.out.println("-----------ssoWeb已启动-----------");
        System.in.read();
        System.out.println("-----------ssoWeb已关闭-----------");
    }

}
