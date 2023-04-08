package site.surui.web.admin;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@Configuration
@EnableSwagger2WebMvc
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig {
    @Bean(value = "dockerBean")
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)  // OAS_30
                .enable(true)  // 仅在开发环境开启Swagger
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("site.surui.web.admin.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("API文档-ADMIN")
                .description("这是描述信息")
                .contact(new Contact("sdudoc", "xxx", "xxxs"))
                .version("1.0")
                .build();
    }
}
