package com.mp.be.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolverChain;

import java.util.List;

@Configuration
public class ResourceHandlerConfig implements WebMvcConfigurer {

    static class IndexFallbackResourceResolver extends PathResourceResolver {
        @Override
        protected Resource resolveResourceInternal(HttpServletRequest request, String requestPath,
                                                   List<? extends Resource> locations, ResourceResolverChain chain) {
            if (requestPath.startsWith("swagger-ui") || requestPath.startsWith("v3/api-docs") || requestPath.startsWith("api/")) {
                return null;
            }
            Resource resource = super.resolveResourceInternal(request, requestPath, locations, chain);
            if (resource == null) {
                resource = super.resolveResourceInternal(request, "/index.html", locations, chain);
            }
            return resource;
        }
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .setOrder(Ordered.LOWEST_PRECEDENCE)
                .addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .resourceChain(true)
                .addResolver(new IndexFallbackResourceResolver());
    }
}