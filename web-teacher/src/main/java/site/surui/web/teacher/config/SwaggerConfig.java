package site.surui.web.teacher.config;

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
                .apis(RequestHandlerSelectors.basePackage("site.surui.web.teacher.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("SDU WEB-TEACHER")
                .description("SDU WEB课程设计教师服务接口文档")
                .contact(new Contact("郭苏睿, 吕俊安, 吴羲勇, 王治松, 许子璇（按首字母顺序）", "web.surui.site", "EMPTY"))
                .version("0.4.0")
                .license("Anti-996")
                .licenseUrl("https://github.com/kattgu7/Anti-996-License")
                .build();
    }
}
