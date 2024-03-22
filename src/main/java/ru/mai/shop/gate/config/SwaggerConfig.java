package ru.mai.shop.gate.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.springframework.util.MimeTypeUtils.ALL_VALUE;

@Configuration
public class SwaggerConfig {

    @Value("${swagger.server.url}")
    private String serverUrl;

    @Value("${swagger.server.description}")
    private String serverDescription;

    @Bean
    public OpenAPI openApi() {
        Info swaggerInfo = new Info()
                .title("Gateway")
                .description("Java standard")
                .version("v0.0.1");

        return new OpenAPI().info(swaggerInfo).addServersItem(getServer());
    }

    private Server getServer() {
        var server = new Server();
        server.setDescription(serverDescription);
        server.setUrl(serverUrl);

        return server;
    }

    @Bean
    public OpenApiCustomizer customerGlobalHeaderOpenApiCustomizer() {
        return openApi -> openApi.getPaths().values().forEach(this::configOperations);
    }

    private void configOperations(PathItem pathItem) {
        pathItem.readOperations().forEach(this::configOperation);
    }

    private void configOperation(Operation operation) {
        ApiResponses apiResponses = operation.getResponses();
        Content content = new Content()
                .addMediaType(ALL_VALUE, new MediaType());
        ApiResponse internalServerErrorApiResponse = new ApiResponse()
                .description("Ошибка")
                .content(content);
        apiResponses.addApiResponse("500", internalServerErrorApiResponse);
    }
}
