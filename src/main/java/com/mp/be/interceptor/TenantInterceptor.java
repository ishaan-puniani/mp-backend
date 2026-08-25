/// File is generated from https://studio.fabbuilder.com - 
package com.mp.be.interceptor;

import com.mp.be.database.entities.Tenant;
import com.mp.be.database.repositories.TenantRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@RequiredArgsConstructor
public class TenantInterceptor implements HandlerInterceptor{

    @Autowired
    TenantRepository tenantRepository;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestUri = request.getRequestURI();
        String tenantPathPrefix = "/api/tenant";

        if (requestUri.equals(tenantPathPrefix) || !requestUri.contains(tenantPathPrefix)|| requestUri.startsWith(tenantPathPrefix + "/invitation")) {
            return true;
        }

        if (!requestUri.startsWith(tenantPathPrefix + "/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request URI format. Expected format: /api/tenant/{tenantId}/entity");
            return false;
        }


        String pathWithoutTenantPrefix = requestUri.substring(tenantPathPrefix.length());
        int tenantIdStartIndex = pathWithoutTenantPrefix.indexOf('/');
        String tenantId = null;
        if (tenantIdStartIndex == -1) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request URI format. Expected format: /api/tenant/{tenantId}/entity");
            return false;
        }else{
            if(tenantIdStartIndex==0){
                pathWithoutTenantPrefix= pathWithoutTenantPrefix.substring(1);
                if(pathWithoutTenantPrefix.contains("/")) {
                    tenantIdStartIndex = pathWithoutTenantPrefix.indexOf('/');
                } else {
                    tenantIdStartIndex = pathWithoutTenantPrefix.length();
                }
            }
        
        tenantId = pathWithoutTenantPrefix.substring(0, tenantIdStartIndex);}

        Tenant tenant = tenantRepository.findById(tenantId).orElse(null);

        request.setAttribute("currentTenant", tenant);

        return true;
    }

}