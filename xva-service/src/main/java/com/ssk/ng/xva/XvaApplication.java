package com.ssk.ng.xva;

import com.ssk.ng.xva.config.WebRootConfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@Import({WebRootConfig.class})
public class XvaApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(XvaApplication.class);
        Map<String, Object> map = new HashMap<>();
        map.put("server.servlet.contextPath", "/ch-client");
        map.put("server.port", "8484");
        application.setDefaultProperties(map);
        application.run(args);
    }
}
