package ru.sauvest.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import ru.sauvest.baseservices.controller.BaseGlobalExceptionHandlerController;
import ru.sauvest.logging.filter.MdcFilter;

@SpringBootApplication
@EnableAsync
@Import({BaseGlobalExceptionHandlerController.class, MdcFilter.class})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
