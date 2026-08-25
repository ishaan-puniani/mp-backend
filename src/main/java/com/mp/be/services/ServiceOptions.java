/// File is generated from https://studio.fabbuilder.com - 
package com.mp.be.services;

import com.mp.be.database.entities.Tenant;
import com.mp.be.database.entities.User;
import jakarta.servlet.http.HttpServletRequest;

public class ServiceOptions {
    private User currentUser;
    private Tenant currentTenant;

    private String currentLanguage;

    public ServiceOptions(HttpServletRequest request) {
        this.currentUser = (User) request.getAttribute("currentUser");
        this.currentTenant = (Tenant) request.getAttribute("currentTenant");
        this.currentLanguage = request.getHeader("Accept-Language");
    }

    public Tenant getCurrentTenant() {
        return currentTenant;
    }
    public String getCurrentTenantId() {
             return currentTenant != null ? currentTenant.getId() : null;
    }
    public User getCurrentUser() {
        return currentUser;
    }
    public String getCurrentUserId() {
       return currentUser != null ? currentUser.getId() : null;
    }
    public String getCurrentLanguage() {
        return currentLanguage;
    }


}
