package com.sriramks.signedurl.service;

import com.sriramks.signedurl.util.SignUrlAction;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.Tuple;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.HttpMethod;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.stereotype.Service;

@Service(value = "gcsImageService")
public class GCSImageService implements ImageService {

  //  private byte[] keyBytes;

  private static final String PRIVATE_KEY_NAME = "gcs image bucket";

  private static final String KEY_PATH = "/Users/a1257317/Sriram/GCP/device-graph/src/main/resources/privatekey/device-graph.p12";

  @Override
  public String getImageUrl(String image) throws Exception {
    StorageOptions.Builder optionsBuilder = StorageOptions.newBuilder();
    Tuple<ServiceAccountCredentials, BlobInfo> parsedValue = SignUrlAction.parse(KEY_PATH, "gcs-image-bucket@device-graph.iam.gserviceaccount.com", "image-urls", image);
    optionsBuilder.setCredentials(parsedValue.x());
    Storage storage = optionsBuilder.build().getService();
    return SignUrlAction.signExistingBlob(storage, parsedValue, HttpMethod.GET);
  }

  @Override
  public String putImageUrl(String image) throws Exception {
    StorageOptions.Builder optionsBuilder = StorageOptions.newBuilder();
    Tuple<ServiceAccountCredentials, BlobInfo> parsedValue = SignUrlAction.parse(KEY_PATH, "gcs-image-bucket@device-graph.iam.gserviceaccount.com", "image-urls", image);
    optionsBuilder.setCredentials(parsedValue.x());
    Storage storage = optionsBuilder.build().getService();
    return SignUrlAction.signNewBlob(storage, parsedValue, HttpMethod.PUT);
  }
}
