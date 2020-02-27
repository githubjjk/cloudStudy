package jjk.csauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaClient
@ComponentScan(basePackages = {"jjk.csauth","jjk.csutils.service"})
public class CsAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(CsAuthApplication.class, args);
    }

}
