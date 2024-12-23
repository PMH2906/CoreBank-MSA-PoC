package org.tmax.banking.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {

        Server server = new Server();
        server.setUrl("/");

        Info info = new Info()
                .title("Banking Swagger API")
                .version("v1.0.0")
                .description("계정계 뱅킹 서비스 스웨거 API");

        return new OpenAPI()
                .info(info)
                .servers(List.of(server));
    }

}

