package com.quark.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.data.web.HateoasSortHandlerMethodArgumentResolver;
import org.springframework.data.web.PagedResourcesAssemblerArgumentResolver;
import org.springframework.hateoas.client.LinkDiscoverer;
import org.springframework.hateoas.client.LinkDiscoverers;
import org.springframework.hateoas.mediatype.collectionjson.CollectionJsonLinkDiscoverer;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.plugin.core.SimplePluginRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableWebMvc
@EnableSwagger2
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${fileupload.max.allowed.size:20971520}")
    private long maxUploadSize;

    @Value("${fileupload.max.inmemory.size:1048576}")
    private int maxInMemorySize;

    @Value("${quark.resource.locations:ui}")
    private String resourceLocations;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(sortResolver());
        argumentResolvers.add(pageableResolver());
        argumentResolvers.add(pagedResourcesAssemblerArgumentResolver());
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/api/v2/api-docs", "/v2/api-docs");
        registry.addRedirectViewController("/api/swagger-resources/configuration/ui", "/swagger-resources/configuration/ui");
        registry.addRedirectViewController("/api/swagger-resources/configuration/security", "/swagger-resources/configuration/security");
        registry.addRedirectViewController("/api/swagger-resources", "/swagger-resources");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html**").addResourceLocations("classpath:/META-INF/resources/swagger-ui.html");
        registry.addResourceHandler("webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");

        registry.addResourceHandler("*.html").addResourceLocations(resourceLocations);
        registry.addResourceHandler("*.js").addResourceLocations(resourceLocations);
        registry.addResourceHandler("*.css").addResourceLocations(resourceLocations);
        registry.addResourceHandler("*.svg").addResourceLocations(resourceLocations);
        registry.addResourceHandler("*.ico").addResourceLocations(resourceLocations);
        registry.addResourceHandler("*.eot").addResourceLocations(resourceLocations);
        registry.addResourceHandler("*.ttf").addResourceLocations(resourceLocations);
        registry.addResourceHandler("*.woff").addResourceLocations(resourceLocations);
    }


    public Docket apiDocket() {

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(getApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.quark.web"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo getApiInfo() {

        return new ApiInfoBuilder()
                .title("Swagger API Doc")
                .description("More description about the API")
                .version("1.0.0")
                .build();
    }

    @Bean
    public HateoasPageableHandlerMethodArgumentResolver pageableResolver() {
        return new HateoasPageableHandlerMethodArgumentResolver(sortResolver());
    }

    @Bean
    public HateoasSortHandlerMethodArgumentResolver sortResolver() {
        return new HateoasSortHandlerMethodArgumentResolver();
    }

    @Bean
    public PagedResourcesAssemblerArgumentResolver pagedResourcesAssemblerArgumentResolver() {
        return new PagedResourcesAssemblerArgumentResolver(pageableResolver(), null);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(createJacksonHttpMessageConverter());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }

    private HttpMessageConverter<Object> createJacksonHttpMessageConverter() {
        ObjectMapper objectMapper = new Jackson2ObjectMapperBuilder().indentOutput(true)
                //.modulesToInstall(JsonConfig.JAVA_TIME_MODULE)
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).build();

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);

        return converter;
    }

    /*
    @Bean
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        return new RequestMappingHandlerMapping();
    }
    */

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setDefaultEncoding("utf-8");
        resolver.setMaxUploadSize(maxUploadSize);
        resolver.setMaxInMemorySize(maxInMemorySize);
        return resolver;
    }


    @Bean
    public LinkDiscoverers discoverers() {
        List<LinkDiscoverer> plugins = new ArrayList<>();
        plugins.add(new CollectionJsonLinkDiscoverer());
        return new LinkDiscoverers(SimplePluginRegistry.create(plugins));

    }
}
