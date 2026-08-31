package com.mp.be.config;

import com.mp.be.interceptor.TenantInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private TenantInterceptor tenantInterceptor;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/swagger-ui.html", "/api/swagger-ui/index.html");
        registry.addRedirectViewController("/swagger-ui", "/api/swagger-ui/index.html");
        registry.addRedirectViewController("/swagger-ui/", "/api/swagger-ui/index.html");
        registry.addRedirectViewController("/swagger", "/api/swagger-ui/index.html");
        registry.addRedirectViewController("/api/swagger-ui", "/api/swagger-ui/index.html");
        registry.addRedirectViewController("/api/swagger-ui/", "/api/swagger-ui/index.html");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tenantInterceptor)
                .addPathPatterns("/api/tenant/**")
                .excludePathPatterns("/swagger-ui/**", "/api/swagger-ui/**", "/v3/api-docs/**", "/api/v3/api-docs/**", "/swagger-ui.html", "/api/swagger-ui.html");
    }
}