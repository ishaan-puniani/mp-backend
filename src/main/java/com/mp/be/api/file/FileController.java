package com.mp.be.api.file;

import com.mp.be.database.GoogleCloudFileStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/tenant/{tenantId}/file")
public class FileController {

    @Autowired
    private GoogleCloudFileStorage googleCloudFileStorage;

    @GetMapping("/credentials")
    public Map<String, Object> getFileCredentials(@RequestParam("filename") String filename, @RequestParam("storageId") String storageId) throws IOException, GeneralSecurityException, URISyntaxException, GeneralSecurityException, URISyntaxException {
        String privateUrl = "product/uploaded/file/97cc18d0-698f-40f4-a81d-7599a2dd9996/" + filename;

        Map<String, Object> uploadCredentials =  googleCloudFileStorage.uploadCredentials(filename, storageId);
        String downloadUrl = googleCloudFileStorage.downloadUrl(filename,storageId);

        Map<String, Object> response = new HashMap<>();
        response.put("privateUrl", privateUrl);
        response.put("downloadUrl",downloadUrl) ;
        response.put("uploadCredentials", uploadCredentials);
        return response;

    }


}
