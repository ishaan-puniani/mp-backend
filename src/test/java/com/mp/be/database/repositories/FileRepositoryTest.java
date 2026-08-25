package com.mp.be.database.repositories;

import com.mp.be.database.entities.File;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class FileRepositoryTest {

    @Autowired
    private FileRepository fileRepository;

    @Test
    void testSaveAndFindById() {
        File file = new File("file1", 1024L, "privateUrl", "publicUrl", "downloadUrl", true);
        fileRepository.save(file);

        File found = fileRepository.findById(file.getId()).orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("file1");
    }

    @Test
    void testDelete() {
        File file = new File("file1", 1024L, "privateUrl", "publicUrl", "downloadUrl", true);
        fileRepository.save(file);
        fileRepository.delete(file);

        File found = fileRepository.findById(file.getId()).orElse(null);
        assertThat(found).isNull();
    }
} 