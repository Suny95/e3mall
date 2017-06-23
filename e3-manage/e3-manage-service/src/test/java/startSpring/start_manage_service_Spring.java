package startSpring;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * Created by Suny on 2017/6/19.
 */
public class start_manage_service_Spring {

    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring/*.xml");

        System.out.println("-----------manageService已启动-----------");
        System.in.read();
        System.out.println("-----------manageService已关闭-----------");
    }

}
