package jjk.csgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaClient
@ComponentScan(basePackages = {"jjk.csgateway","jjk.csutils.service"})
public class CsGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(CsGatewayApplication.class, args);
    }

}
