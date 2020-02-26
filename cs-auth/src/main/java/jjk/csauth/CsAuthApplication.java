package jjk.csauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication(scanBasePackages = "jjk.csutils.service")
@EnableEurekaClient
public class CsAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(CsAuthApplication.class, args);
    }

}
