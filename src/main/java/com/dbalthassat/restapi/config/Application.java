package com.dbalthassat.restapi.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@EnableConfigurationProperties
@SpringBootApplication
@ComponentScan({
        Application.BASE_PACKAGE + ".config",
        Application.BASE_PACKAGE + ".controller",
        Application.BASE_PACKAGE + ".mapper",
        Application.BASE_PACKAGE + ".property",
        Application.BASE_PACKAGE + ".service",
        Application.BASE_PACKAGE + ".repository"
})
public class Application extends SpringBootServletInitializer {
    public static final String BASE_PACKAGE = "com.dbalthassat.restapi";
    public static final String DEV_ENVIRONMENT = "development";

    /**
     * Start the web app on a Spring Boot's embedded Tomcat.
     *
     * @param args maven arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * Start the web app on an usual application server.
     *
     * @param application Spring Boot builder
     * @return the configuration to be launched on the server
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }
}
