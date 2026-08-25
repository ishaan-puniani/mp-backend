package com.mp.be.services.settings;

import com.mp.be.database.entities.Setting;
import com.mp.be.database.entities.File;
import com.mp.be.database.entities.Tenant;
import com.mp.be.database.repositories.AuditLogRepository;
import com.mp.be.database.repositories.FileRepository;
import com.mp.be.database.repositories.SettingsRepository;
import com.mp.be.database.repositories.TenantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SettingsServiceImplTest {

    @Mock
    private SettingsRepository settingsRepository;

    @Mock
    private AuditLogRepository auditLogRepository;

    @Mock
    private FileRepository fileRepository;

    @Mock
    private TenantRepository tenantRepository;

    @InjectMocks
    private SettingsServiceImpl settingsService;

    private Setting setting;
    private Tenant tenant;

    @BeforeEach
    void setUp() {
        setting = new Setting();
        setting.setId("1");
        tenant = new Tenant();
        tenant.setId("tenant1");
    }

    @Test
    void testFind() {
        when(settingsRepository.findById("1")).thenReturn(Optional.of(setting));
        Setting foundSetting = settingsService.find("1");
        assertNotNull(foundSetting);
        assertEquals("1", foundSetting.getId());
    }

    @Test
    void testFindOrCreate() {
        when(settingsRepository.findByTenantId("tenant1")).thenReturn(null);
        when(settingsRepository.save(any(Setting.class))).thenReturn(setting);
        Setting result = settingsService.findOrCreate(tenant);
        assertNotNull(result);
        assertEquals("1", result.getId());
        verify(settingsRepository, times(1)).save(any(Setting.class));
    }

    @Test
    void testCreate() {
        when(settingsRepository.save(setting)).thenReturn(setting);
        Setting createdSetting = settingsService.create(setting);
        assertNotNull(createdSetting);
        assertEquals("1", createdSetting.getId());
        verify(auditLogRepository, times(1)).save(any());
    }

    @Test
    void testDelete() {
        when(settingsRepository.findById("1")).thenReturn(Optional.of(setting));
        doNothing().when(settingsRepository).delete(setting);
        settingsService.delete("1");
        verify(settingsRepository, times(1)).delete(setting);
        verify(auditLogRepository, times(1)).save(any());
    }

    @Test
    void testUpdate() {
        when(settingsRepository.findById("1")).thenReturn(Optional.of(setting));
        when(settingsRepository.save(setting)).thenReturn(setting);
        Setting updatedSetting = settingsService.update("1", setting);
        assertNotNull(updatedSetting);
        assertEquals("1", updatedSetting.getId());
        verify(settingsRepository, times(1)).save(setting);
        verify(auditLogRepository, times(1)).save(any());
    }

    @Test
    void testUpdateWithNewFiles() {
        // Arrange
        File newFile = new File();
        newFile.setId(null); // Simulate new file without ID
        Setting data = new Setting();
        data.setLogos(List.of(newFile));

        when(settingsRepository.findById("1")).thenReturn(Optional.of(new Setting()));
        when(fileRepository.findById(isNull())).thenReturn(Optional.empty());
        when(fileRepository.save(any(File.class))).thenReturn(newFile);

        // Act
        settingsService.update("1", data);

        // Assert
        verify(fileRepository, times(1)).save(newFile);
    }

    @Test
    void testUpdateWithExistingFiles() {
        // Arrange
        File existingFile = new File();
        existingFile.setId("existingId");
        Setting data = new Setting();
        data.setLogos(List.of(existingFile));

        when(settingsRepository.findById("1")).thenReturn(Optional.of(new Setting()));
        when(fileRepository.findById(existingFile.getId())).thenReturn(Optional.of(existingFile));

        // Act
        settingsService.update("1", data);

        // Assert
        verify(fileRepository, never()).save(existingFile);
    }

    @Test
    void testUpdateWithLogosAndBackgroundImages() {
        // Arrange
        Setting data = new Setting();
        File logo = new File();
        logo.setId(null);
        File backgroundImage = new File();
        backgroundImage.setId(null);
        data.setLogos(List.of(logo));
        data.setBackgroundImages(List.of(backgroundImage));

        Setting record = new Setting();
        when(settingsRepository.findById("1")).thenReturn(Optional.of(record));
        when(fileRepository.findById(isNull())).thenReturn(Optional.empty());
        when(fileRepository.save(any(File.class))).thenReturn(logo, backgroundImage);

        // Act
        settingsService.update("1", data);

        // Assert
        assertNotNull(record.getLogos());
        assertNotNull(record.getBackgroundImages());
        verify(fileRepository, times(2)).save(any(File.class));
    }

    @Test
    void testUpdateWithNullLogosAndBackgroundImages() {
        // Arrange
        Setting data = new Setting();
        data.setLogos(null);
        data.setBackgroundImages(null);

        Setting record = new Setting();
        when(settingsRepository.findById("1")).thenReturn(Optional.of(record));

        // Act
        settingsService.update("1", data);

        // Assert
        assertNotNull(record.getLogos());
        assertNotNull(record.getBackgroundImages());
        assertEquals(0, record.getLogos().size());
        assertEquals(0, record.getBackgroundImages().size());
    }
} 