package com.microservico.pagamentoservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.microservico.pagamentoservice.domain.repository")
@OpenAPIDefinition(
    info = @Info(
        title = "MicroServico Pagamento-Service",
        description = "API para gerenciamento e processamento de pagamentos",
        version = "1.0"
    )
)
@SecurityScheme(
    name = "jwt_auth",
    scheme = "bearer",
    bearerFormat = "JWT",
    type = SecuritySchemeType.HTTP,
    in = SecuritySchemeIn.HEADER
)
public class PagamentoServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PagamentoServiceApplication.class, args);
    }
}
