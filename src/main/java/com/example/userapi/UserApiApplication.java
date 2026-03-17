package com.example.userapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The entry point for the Spring Boot application.
 *
 * @SpringBootApplication is a convenience annotation that combines three annotations:
 *   1. @Configuration    - Marks this class as a source of bean definitions for the application context.
 *   2. @EnableAutoConfiguration - Tells Spring Boot to automatically configure beans based on the
 *                                 dependencies on the classpath (e.g. it sees spring-boot-starter-web
 *                                 and auto-configures an embedded Tomcat server).
 *   3. @ComponentScan    - Tells Spring to scan this package and all sub-packages (model, repository,
 *                          service, controller) for classes annotated with @Component, @Service,
 *                          @Repository, @Controller, etc., and register them as beans.
 */
@SpringBootApplication
public class UserApiApplication {

    /**
     * Standard Java main method - the JVM calls this to start the program.
     * SpringApplication.run() bootstraps the entire Spring framework:
     *   - Creates the ApplicationContext (the Spring container that holds all beans)
     *   - Starts the embedded Tomcat web server on port 8080
     *   - Scans for and initialises all components, services, controllers, etc.
     *   - Connects to the H2 database and creates tables based on @Entity classes
     */
    public static void main(String[] args) {
        SpringApplication.run(UserApiApplication.class, args);
    }
}
