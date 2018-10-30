package xyz.feijing.singlepage.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

import static springfox.documentation.builders.PathSelectors.regex;

/**
 * @author feijing
 * @date 2018年10月25日15:58:13
 * @version 1.0.0
 */
@Slf4j
@Configuration
@EnableSwagger2
public class SwaggerAutoConfiguration {

    @Bean
    public Docket productApi() {
        log.info("init swagger");
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage("xyz.feijing.singlepage"))
            .paths(regex("/.*"))
            .build()
            .apiInfo(metaData());
    }

    private ApiInfo metaData() {
        ApiInfo apiInfo = new ApiInfo(
            "Single Page Api",
            "Single Page Api",
            "1.0.0",
            null,
            new Contact("feijing", "http://github.com/zfeidy", "zfeidy@163.com"),
            "",
            null,
            Collections.emptyList());
        return apiInfo;
    }
}
