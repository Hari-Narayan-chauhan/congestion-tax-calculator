package congestion.tax.calculator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurationSupport{
	
	 @Bean
	    public Docket apiRboRestServer() {
	        return new Docket(DocumentationType.SWAGGER_2)
	                .groupName("Congestion_tax_calculator_documentation")
	                .select()
	                .apis(RequestHandlerSelectors.basePackage("congestion.tax.calculator.controller"))
	                .paths(PathSelectors.ant("/api/**"))
	                .build();
	                
	    }
	 
	    @Override
	    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
	        registry.addResourceHandler("swagger-ui.html")
	                .addResourceLocations("classpath:/META-INF/resources/");

	        registry.addResourceHandler("/webjars/**")
	                .addResourceLocations("classpath:/META-INF/resources/webjars/");
	    }

}
