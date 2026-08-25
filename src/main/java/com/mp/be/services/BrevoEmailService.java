package com.mp.be.services;

import com.mp.be.config.BrevoConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BrevoEmailService {

    @Autowired
    private BrevoConfig brevoConfig;

    private static final String BREVO_API_URL = "https://api.sendinblue.com/v3/smtp/email";

    public void sendEmail(String recipientEmail, String templateId, Map<String, Object> variables) {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> emailData = new HashMap<>();

        List<Map<String, String>> toList = List.of(
                new HashMap<String, String>() {{
                    put("email", recipientEmail);
                    put("name", "User");
                }}
        );

        emailData.put("to", toList);
        emailData.put("sender", new HashMap<String, String>() {{
            put("name", "FAB Builder");
            put("email", brevoConfig.getEmailFrom());
        }});
        emailData.put("templateId", Integer.parseInt(templateId));
        emailData.put("params", variables);

        try {
            restTemplate.postForEntity(BREVO_API_URL, new HttpEntity<>(emailData, createHeaders()), String.class);
            System.out.println("Email sent successfully to " + recipientEmail);
        } catch (Exception e) {
            System.err.println("Error sending email: " + e.getMessage());
        }
    }


    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", brevoConfig.getApiKey());
        return headers;
    }
}
