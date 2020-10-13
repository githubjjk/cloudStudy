package jjk.cscar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaClient
@ComponentScan(value = {"jjk.cscar","jjk.csutils"})
public class CsCarApplication {

    public static void main(String[] args) {
        SpringApplication.run(CsCarApplication.class, args);
    }

}
