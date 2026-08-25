package com.mp.be.database.entities;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.List;

public class SettingTest {

    @Test
    public void testSettingCreation() {
        Setting setting = new Setting("dark", null, null);
        assertNotNull(setting);
        assertEquals("dark", setting.getTheme());
    }

    @Test
    public void testEqualsAndHashCodeWithDifferentObjects() {
        Setting setting1 = new Setting("dark", null, null);
        Setting setting2 = new Setting("light", null, null);
        assertNotEquals(setting1, setting2);
        assertNotEquals(setting1.hashCode(), setting2.hashCode());

        assertNotEquals(setting1, null);
        assertNotEquals(setting1, new Object());
    }

    @Test
    public void testEqualsAndHashCode() {
        Setting setting1 = new Setting("dark", null, null);
        Setting setting2 = new Setting("dark", null, null);
        assertEquals(setting1, setting2);
        assertEquals(setting1.hashCode(), setting2.hashCode());
    }

    @Test
    public void testEqualsWithDifferentFields() {
        Setting setting1 = new Setting("dark", null, null);
        Setting setting2 = new Setting("light", null, null);
        assertNotEquals(setting1, setting2);
    }

    @Test
    public void testEqualsWithNullFields() {
        Setting setting1 = new Setting(null, null, null);
        Setting setting2 = new Setting(null, null, null);
        assertEquals(setting1, setting2);
    }

} 