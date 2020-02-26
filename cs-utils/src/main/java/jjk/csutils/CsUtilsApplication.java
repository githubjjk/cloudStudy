package jjk.csutils;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaClient
@ComponentScan(basePackages = {"jjk.csutils.service"})
public class CsUtilsApplication {

    public static void main(String[] args) {
        SpringApplication.run(CsUtilsApplication.class, args);
    }

}
