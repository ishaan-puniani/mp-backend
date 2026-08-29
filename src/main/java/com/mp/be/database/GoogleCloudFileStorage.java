package com.mp.be.database;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.common.io.BaseEncoding;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.Signature;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class GoogleCloudFileStorage {

    @Value("${google.cloud.storage.bucket}")
    private String bucketName;

    private Storage storage;
    private ServiceAccountCredentials serviceAccountCredentials;

    public GoogleCloudFileStorage(@Value("${google.cloud.storage.bucket}") String bucketName,
                                  @Value("${google.cloud.credentials.json}") String credentialsJson) throws IOException {
        this.bucketName = bucketName;

        try (ByteArrayInputStream credentialsStream = new ByteArrayInputStream(credentialsJson.getBytes(StandardCharsets.UTF_8))) {
            GoogleCredentials googleCredentials = GoogleCredentials.fromStream(credentialsStream);
            if (googleCredentials instanceof ServiceAccountCredentials) {
                this.serviceAccountCredentials = (ServiceAccountCredentials) googleCredentials;
                // Initialize the Storage instance here
                this.storage = StorageOptions.newBuilder()
                        .setCredentials(googleCredentials)
                        .build().getService();
            } else {
                throw new IOException("Credentials are not service account credentials");
            }
        }
    }

    public Map<String, Object> uploadCredentials(String filename, String storageId) throws IOException, GeneralSecurityException, URISyntaxException {
        String key = storageId + "/" + filename;
        BlobId blobId = BlobId.of(bucketName, key);
        String acl = "public-read";
        String xGoogDate = getCurrentUtcDate();
        String shortDate = xGoogDate.substring(0, 8);
        String xGoogCredential = String.format("%s/%s/auto/storage/goog4_request",
                serviceAccountCredentials.getClientEmail(), shortDate);
        String xGoogAlgorithm = "GOOG4-RSA-SHA256";

        String policy = createPolicyDocument(acl, key, xGoogDate, xGoogCredential, xGoogAlgorithm);
        String policyBase64 = BaseEncoding.base64().encode(policy.getBytes(StandardCharsets.UTF_8));
        String xGoogSignature = signPolicy(policyBase64, serviceAccountCredentials.getPrivateKey());

        Map<String, String> fields = new HashMap<>();
        fields.put("acl", acl);
        fields.put("key", key);
        fields.put("x-goog-date", xGoogDate);
        fields.put("x-goog-credential", xGoogCredential);
        fields.put("x-goog-algorithm", xGoogAlgorithm);
        fields.put("policy", policyBase64);
        fields.put("x-goog-signature", xGoogSignature);

        Map<String, Object> result = new HashMap<>();
        result.put("url", "https://" + bucketName + ".storage.googleapis.com/");
        result.put("fields", fields);
        result.put("publicUrl", "https://storage.googleapis.com/" + bucketName + "/" + storageId + "/" + filename);

        return result;
    }

    public String downloadUrl(String filename, String storageId) {
        return "https://storage.googleapis.com/" + bucketName + "/" + storageId + "/" + filename;
    }


    private String getCurrentUtcDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(new Date());
    }

    private String createPolicyDocument(String acl, String key, String xGoogDate, String xGoogCredential, String xGoogAlgorithm) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 1); // Policy expiration time set to 1 hour from now
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String expiration = sdf.format(calendar.getTime());

        return String.format("{ \"expiration\": \"%s\", \"conditions\": [ " +
                        "{\"bucket\": \"%s\"}, " +
                        "{\"key\": \"%s\"}, " +
                        "{\"acl\": \"%s\"}, " +
                        "[\"content-length-range\", 0, 10485760], " +
                        "{\"x-goog-date\": \"%s\"}, " +
                        "{\"x-goog-credential\": \"%s\"}, " +
                        "{\"x-goog-algorithm\": \"%s\"} ] }",
                expiration, bucketName, key, acl, xGoogDate, xGoogCredential, xGoogAlgorithm);
    }

    private String signPolicy(String policy, PrivateKey privateKey) throws GeneralSecurityException {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(policy.getBytes(StandardCharsets.UTF_8));
        byte[] signedPolicy = signature.sign();
        return BaseEncoding.base16().lowerCase().encode(signedPolicy);
    }

}