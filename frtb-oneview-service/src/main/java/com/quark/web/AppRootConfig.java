package com.quark.web;


import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;
import java.util.TimeZone;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@PropertySources({@PropertySource(value = "classpath:quark.properties", ignoreResourceNotFound = true),
        @PropertySource(value = "classpath:system.properties", ignoreResourceNotFound = true)})
@ImportResource("classpath:quark-workflow.xml")
public class AppRootConfig {

    static {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        List<String> jvmArgs = runtimeMXBean.getInputArguments();
        boolean commandLine = jvmArgs.stream().anyMatch(s -> s.startsWith("-Duser.timezone="));
        if (!commandLine) {
            log.info("App TimeZone not set. Setting to UTC as default");
            System.setProperty("user.timezone", "UTC");
            TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        }
        log.info("TimeZone = {}", TimeZone.getDefault());
    }

    public static String getEnv() {
        String envName = System.getProperty("env");
        if (StringUtils.isEmpty(envName)) {
            envName = "dev";
            System.setProperty("env", envName);
        }
        return envName;
    }

    @Bean
    public PropertySourcesPlaceholderConfigurer appPropertyConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }


    /*
    @Bean
    public FilterRegistrationBean authFilter() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setDispatcherTypes(EnumSet.of(DispatcherType.REQUEST));
        registrationBean.setFilter(new JwtFilter());
        registrationBean.addUrlPatterns("/services/*");
        return registrationBean;
    }
    */
}
