package ru.sauvest.market;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.Import;
import ru.sauvest.baseservices.controller.BaseGlobalExceptionHandlerController;

@SpringBootApplication(exclude= {UserDetailsServiceAutoConfiguration.class})
@Import(BaseGlobalExceptionHandlerController.class)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
