package jjk.csgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication(scanBasePackages = "jjk.csutils.service")
@EnableEurekaClient
public class CsGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(CsGatewayApplication.class, args);
    }

}
