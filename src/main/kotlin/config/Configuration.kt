package config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.config.EnableIntegration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket

// documentation: http://127.0.0.1:8080/swagger-ui/
@Configuration
@EnableIntegration
class Configuration {
    @Bean
    fun swagger(): Docket =
        Docket(DocumentationType.SWAGGER_2)
            .apiInfo(getApiInfo())
            .select()
            .apis(RequestHandlerSelectors.basePackage("client.api"))
            .paths(PathSelectors.ant("/cqrs/**"))
            .build()

    private fun getApiInfo(): ApiInfo {
        val contact = Contact("CQRSBank", "https://github.com/Dimch84/CQRSBank", "example@gmail.com")
        return ApiInfoBuilder()
            .title("CQRSBank documentation")
            .description("there is a documentation")
            .version("1.0.0")
            .contact(contact)
            .build()
    }
}
