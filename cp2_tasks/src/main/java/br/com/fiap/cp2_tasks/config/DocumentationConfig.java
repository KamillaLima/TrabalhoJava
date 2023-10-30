package br.com.fiap.cp2_tasks.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;

/**
 * Configuração da documentação da API.
 * 
 * @author Kamilla
 * @version 1.0
 */
@Configuration
public class DocumentationConfig {

    /**
     * Define as informações da API, incluindo título, contato, versão e descrição.
     *
     * @return Uma instância de OpenAPI contendo as informações da API.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("API Cp2_Tasks")
                .contact(new Contact()
                    .email("kami@gmail.com.br")
                    .name("Kamilla")
                )
                .version("v1")
                .description("Uma API para cadastro de tarefas")
            )
            .components(new Components()
                .addSecuritySchemes("bearer-key",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                )
            );
    }
}
