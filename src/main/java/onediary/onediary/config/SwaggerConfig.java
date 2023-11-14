package onediary.onediary.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
    }
    private Info apiInfo(){
        return new Info()
                .title("OneDiary API Document")
                .version("0.0.1")
                .description("OneDiary의 API 명세서입니다.");
    }
}

