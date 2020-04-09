package com.finartz.homework.TicketService.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig {
    public static final String TAG_1 = "Airline Controller";
    public static final String TAG_2 = "Airport Controller";
    public static final String TAG_3 = "Flight Controller";
    public static final String TAG_4 = "Ticket Controller";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors
                        .basePackage("com.finartz.homework.TicketService.controller"))
                .paths(PathSelectors.regex("/.*"))
                .build().apiInfo(apiEndPointsInfo())
                .tags(new Tag(TAG_1, "Operations about airlines."))
                .tags(new Tag(TAG_2, "Operations about airports."))
                .tags(new Tag(TAG_3, "Operations about flights."))
                .tags(new Tag(TAG_4, "Operations about tickets."));
    }

    private ApiInfo apiEndPointsInfo() {
        return new ApiInfoBuilder().title("Spring Boot - Ticket Restful Service")
                .description("Description")
                .contact(new Contact("Hasan Cerit", "https://github.com/hasancerit", "hasan__cerit@hotmail.com"))
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                .version("1.0.0")
                .build();
    }
}
