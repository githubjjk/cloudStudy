package jjk.csutils;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class CsUtilsApplication {

    public static void main(String[] args) {
        SpringApplication.run(CsUtilsApplication.class, args);
    }

}
