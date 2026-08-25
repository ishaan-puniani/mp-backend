package com.mp.be.models.settings;

import com.mp.be.database.entities.File;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SettingsModelTest {

    @Test
    public void testSettingsModelGettersAndSetters() {
        SettingsModel settings = new SettingsModel();

        settings.setId("test-id");
        assertEquals("test-id", settings.getId());

        settings.setTheme("dark");
        assertEquals("dark", settings.getTheme());

        List<File> logos = List.of(new File());
        settings.setLogos(logos);
        assertEquals(logos, settings.getLogos());

        List<File> backgroundImages = List.of(new File());
        settings.setBackgroundImages(backgroundImages);
        assertEquals(backgroundImages, settings.getBackgroundImages());

        Date createdAt = new Date();
        settings.setCreatedAt(createdAt);
        assertEquals(createdAt, settings.getCreatedAt());

        Date updatedAt = new Date();
        settings.setUpdatedAt(updatedAt);
        assertEquals(updatedAt, settings.getUpdatedAt());

        settings.setCreatedBy("creator");
        assertEquals("creator", settings.getCreatedBy());

        settings.setUpdatedBy("updater");
        assertEquals("updater", settings.getUpdatedBy());
    }

    @Test
    public void testEqualsAndHashCode() {
        SettingsModel settings1 = new SettingsModel("test-id", "dark", List.of(new File()), List.of(new File()), new Date(), new Date(), "creator", "updater");
        SettingsModel settings2 = new SettingsModel("test-id", "dark", List.of(new File()), List.of(new File()), new Date(), new Date(), "creator", "updater");
        SettingsModel settings3 = new SettingsModel("different-id", "light", List.of(new File()), List.of(new File()), new Date(), new Date(), "creator", "updater");

        assertEquals(settings1, settings2);
        assertNotEquals(settings1, settings3);
        assertEquals(settings1.hashCode(), settings2.hashCode());
        assertNotEquals(settings1.hashCode(), settings3.hashCode());
    }

    @Test
    public void testToString() {
        SettingsModel settings = new SettingsModel("test-id", "dark", List.of(new File()), List.of(new File()), new Date(), new Date(), "creator", "updater");
        String expectedString = "SettingsModel(id=test-id, theme=dark, logos=[File(name=null, sizeInBytes=null, privateUrl=null, publicUrl=null, downloadUrl=null, isnew=true)], backgroundImages=[File(name=null, sizeInBytes=null, privateUrl=null, publicUrl=null, downloadUrl=null, isnew=true)], createdAt=" + settings.getCreatedAt() + ", updatedAt=" + settings.getUpdatedAt() + ", createdBy=creator, updatedBy=updater)";
        assertEquals(expectedString, settings.toString());
    }
}