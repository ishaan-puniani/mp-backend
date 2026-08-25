package com.mp.be.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BrevoConfig {

    @Value("${brevo.api.key}")
    private String apiKey;

    @Value("${brevo.email.from}")
    private String emailFrom;

    public String getApiKey() {
        return apiKey;
    }

    public String getEmailFrom() {
        return emailFrom;
    }
}
