package com.microservico.pagamentoservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        // Informações gerais da API de Pedido
        OpenAPI openAPI = new OpenAPI()
                .info(new Info()
                        .title("API de Gestão de Pedidos")
                        .description("API responsável pelo recebimento e processamento de pedidos de clientes")
                        .version("1.0"));

        // Configuração de segurança JWT
        openAPI.addSecurityItem(new SecurityRequirement().addList("jwt_auth"))
               .schemaRequirement("jwt_auth", createSecurityScheme());

        return openAPI;
    }

    // Definição do esquema de segurança JWT (Bearer Token)
    private SecurityScheme createSecurityScheme() {
        return new SecurityScheme()
                .name("jwt_auth")
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");
    }
}
