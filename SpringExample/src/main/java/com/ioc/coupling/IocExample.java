package com.ioc.coupling;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class IocExample {
    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext("applicationIocBean.xml");

        UserManager userManagerWithDB = (UserManager) context.getBean("userManagerWithDatabaseProvider");
        System.out.println(userManagerWithDB.getUserInfo());


        UserManager userManagerWithWS = (UserManager) context.getBean("userManagerWithWebServiceProvider");
        System.out.println(userManagerWithWS.getUserInfo());


        UserManager userManagerWithNewDB = (UserManager) context.getBean("userManagerWithNewDatabaseProvider");
        System.out.println(userManagerWithNewDB.getUserInfo());
    }
}
