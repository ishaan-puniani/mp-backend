package com.mp.be.api.file;

import com.mp.be.api.file.FileController;
import com.mp.be.database.GoogleCloudFileStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FileControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GoogleCloudFileStorage googleCloudFileStorage;

    @InjectMocks
    private FileController fileController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(fileController).build();
    }

    @Test
    public void testGetFileCredentials() throws Exception {
        String filename = "testfile.txt";
        String storageId = "testStorageId";

        Map<String, Object> mockUploadCredentials = new HashMap<>();
        mockUploadCredentials.put("url", "https://example.com");
        mockUploadCredentials.put("fields", new HashMap<>());

        when(googleCloudFileStorage.uploadCredentials(filename, storageId)).thenReturn(mockUploadCredentials);
        when(googleCloudFileStorage.downloadUrl(filename, storageId)).thenReturn("https://example.com/download");

        mockMvc.perform(get("/api/tenant/1/file/credentials")
                .param("filename", filename)
                .param("storageId", storageId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.privateUrl").value("product/uploaded/file/97cc18d0-698f-40f4-a81d-7599a2dd9996/" + filename))
                .andExpect(jsonPath("$.downloadUrl").value("https://example.com/download"))
                .andExpect(jsonPath("$.uploadCredentials.url").value("https://example.com"));
    }

    @Test
    public void testUploadCredentials() throws Exception {
        String filename = "testfile.txt";
        String storageId = "testStorageId";

        Map<String, Object> mockUploadCredentials = new HashMap<>();
        mockUploadCredentials.put("url", "https://example.com");
        mockUploadCredentials.put("fields", new HashMap<>());

        when(googleCloudFileStorage.uploadCredentials(filename, storageId)).thenReturn(mockUploadCredentials);

        mockMvc.perform(get("/api/tenant/1/file/credentials")
                .param("filename", filename)
                .param("storageId", storageId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uploadCredentials.url").value("https://example.com"));
    }

} 