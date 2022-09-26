//package no.fintlabs;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.web.bind.annotation.RestController;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//
//import static springfox.documentation.builders.RequestHandlerSelectors.withClassAnnotation;
//
//@Configuration
//public class SwaggerConfiguration {
//
//    @Bean
//    public Docket config() {
//        return new Docket(DocumentationType.OAS_30)
//                .select()
//                .apis(withClassAnnotation(RestController.class))
//                .build()
//                .ignoredParameterTypes(
//                        ServerHttpRequest.class
//                );
//    }
//}
