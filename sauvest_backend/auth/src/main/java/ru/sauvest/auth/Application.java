package ru.sauvest.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import ru.sauvest.baseservices.controller.BaseGlobalExceptionHandlerController;

@SpringBootApplication
@EnableAsync
@Import(BaseGlobalExceptionHandlerController.class)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
