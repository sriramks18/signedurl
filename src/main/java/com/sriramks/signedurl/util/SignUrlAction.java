package com.sriramks.signedurl.util;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.Tuple;
import com.google.cloud.storage.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

public class SignUrlAction {

  private static final char[] PASSWORD = "notasecret".toCharArray();

  public static String signExistingBlob(Storage storage, Tuple<ServiceAccountCredentials, BlobInfo> tuple, HttpMethod method) throws Exception {
    return signExistingBlob(storage, tuple.x(), tuple.y(), 15L, TimeUnit.MINUTES, method);
  }

  private static String signExistingBlob(Storage storage, ServiceAccountCredentials cred, BlobInfo blobInfo, Long duration, TimeUnit timeUnit, HttpMethod method) {
    Blob blob = storage.get(blobInfo.getBlobId());
    return blob.signUrl(duration, timeUnit, Storage.SignUrlOption.signWith(cred), Storage.SignUrlOption.httpMethod(method)).toString();
  }

  public static String signNewBlob(Storage storage, Tuple<ServiceAccountCredentials, BlobInfo> tuple, HttpMethod method) throws Exception {
    return signNewBlob(storage, tuple.x(), tuple.y(), 3600L, TimeUnit.MINUTES, method);
  }

  private static String signNewBlob(Storage storage, ServiceAccountCredentials cred, BlobInfo blobInfo, Long duration, TimeUnit timeUnit, HttpMethod method) {
    return storage.signUrl(blobInfo, duration, timeUnit, Storage.SignUrlOption.signWith(cred), Storage.SignUrlOption.httpMethod(method)).toString();
  }

   public static Tuple<ServiceAccountCredentials, BlobInfo>  parse(String keyFilePath, String clientEmail, String bucketName, String fileName) throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {
    KeyStore keystore = KeyStore.getInstance("PKCS12");
    keystore.load(Files.newInputStream(Paths.get(keyFilePath)), PASSWORD);
    PrivateKey privateKey = (PrivateKey) keystore.getKey("privatekey", PASSWORD);
    ServiceAccountCredentials credentials = ServiceAccountCredentials.newBuilder().setClientEmail(clientEmail).setPrivateKey(privateKey).build();
    return Tuple.of(credentials, BlobInfo.newBuilder(BlobId.of(bucketName, fileName)).build());
  }

  public String params() {
    return "<service_account_private_key_file> <service_account_email> <bucket> <path>";
  }
}

