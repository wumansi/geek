package com.example.demo.assembly;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestAssembly {
    public static void main(String[] args){
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        User user = (User) context.getBean("user");
        user.print();

        Person person = context.getBean(Person.class);
        person.say();

        UserService service = context.getBean(UserService.class);
        service.print();

        UserController controller = context.getBean(UserController.class);
        controller.print();

        UserDao dao = context.getBean(UserDao.class);
        dao.queryUser();
    }
}
