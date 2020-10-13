package jjk.cspubliccomponent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaClient
@ComponentScan(basePackages = {"jjk.cspubliccomponent", "jjk.csutils"})
@EnableFeignClients
public class CsPublicComponentApplication {

    public static void main(String[] args) {
        SpringApplication.run(CsPublicComponentApplication.class, args);
    }

}
