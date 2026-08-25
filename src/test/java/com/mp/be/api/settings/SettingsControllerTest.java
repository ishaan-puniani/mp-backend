package com.mp.be.api.settings;

import com.mp.be.database.entities.Setting;
import com.mp.be.database.entities.Tenant;
import com.mp.be.models.settings.SettingsDataModel;
import com.mp.be.services.settings.SettingsService;
import com.mp.be.services.ServiceOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class SettingsControllerTest {

    @Mock
    private SettingsService settingsService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private SettingsController settingsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFind() {
        Setting setting = new Setting();
        when(settingsService.findOrCreate(any())).thenReturn(setting);

        ResponseEntity<Setting> response = settingsController.find(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(setting, response.getBody());
    }

    @Test
    void testFindOrCreate() {
        Setting setting = new Setting();
        Tenant currentTenant = new Tenant();
        when(request.getAttribute("currentTenant")).thenReturn(currentTenant);

        // Success scenario
        when(settingsService.findOrCreate(currentTenant)).thenReturn(setting);
        ResponseEntity<Setting> response = settingsController.find(request);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(setting, response.getBody());

        // Failure scenario
        when(settingsService.findOrCreate(currentTenant)).thenThrow(new RuntimeException("Find or create failed"));
        try {
            response = settingsController.find(request);
        } catch (RuntimeException e) {
            assertEquals("Find or create failed", e.getMessage());
        }
    }

    @Test
    void testFindById() {
        String settingId = "1";
        Setting setting = new Setting();

        // Success scenario
        when(settingsService.find(settingId)).thenReturn(setting);
        Setting response = settingsController.find(settingId);
        assertEquals(setting, response);

        // Failure scenario
        when(settingsService.find(settingId)).thenThrow(new RuntimeException("Setting not found"));
        try {
            response = settingsController.find(settingId);
        } catch (RuntimeException e) {
            assertEquals("Setting not found", e.getMessage());
        }
    }

    @Test
    void testUpdate() {
        Setting setting = new Setting();
        SettingsDataModel settingsDataModel = new SettingsDataModel();
        settingsDataModel.settings = setting;

        // Success scenario
        when(settingsService.update(setting.id, setting)).thenReturn(setting);
        Setting response = settingsController.update(request, settingsDataModel);
        assertEquals(setting, response);

        // Failure scenario
        when(settingsService.update(setting.id, setting)).thenThrow(new RuntimeException("Update failed"));
        try {
            response = settingsController.update(request, settingsDataModel);
        } catch (RuntimeException e) {
            assertEquals("Update failed", e.getMessage());
        }
    }

} 