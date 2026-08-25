/// File is generated from https://studio.fabbuilder.com - 
package com.mp.be.config;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolverChain;

@Configuration
public class ResourceHandlerConfig implements WebMvcConfigurer {

    static class IndexFallbackResourceResolver extends PathResourceResolver{
        @Override
        protected Resource resolveResourceInternal(HttpServletRequest request, String requestPath,
                                                   List<? extends Resource> locations, ResourceResolverChain chain) {
            Resource resource = super.resolveResourceInternal(request, requestPath, locations, chain);
            if(resource==null){
                //try with /index.html
                resource = super.resolveResourceInternal(request,   "/index.html", locations, chain);
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
                //first time resolved, that route will always be used from cache
                .resourceChain(true)
                .addResolver(new IndexFallbackResourceResolver());
    }
}