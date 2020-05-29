package ru.otus.spring.components;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import ru.otus.entities.PhoneDataSet;
import ru.otus.entities.User;
import ru.otus.services.AdminUserService;

import java.util.Arrays;

@Component
public class AdminUser implements ApplicationListener<ContextRefreshedEvent> {
    private final AdminUserService userService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        InitAdminUser();
    }

    public AdminUser(AdminUserService adminUserService) {
        this.userService = adminUserService;
    }

    private void InitAdminUser(){
        User adminUser = new User("Administrator", "admin", "admin");
        adminUser.setPhones(Arrays.asList(new PhoneDataSet("(495) 937-99-92")));
        userService.create(adminUser);
    }
}
