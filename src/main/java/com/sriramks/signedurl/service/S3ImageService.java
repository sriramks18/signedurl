package com.sriramks.signedurl.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.URL;

@Service(value = "s3ImageService")
public class S3ImageService implements ImageService {

  private AmazonS3 s3Client;

  @Override
  public String getImageUrl(String image) throws Exception {
    java.util.Date expiration = new java.util.Date();
    long msec = expiration.getTime();
    msec += 1000 * 60 * 15; // 15 mins.
    expiration.setTime(msec);

    GeneratePresignedUrlRequest generatePresignedUrlRequest =
        new GeneratePresignedUrlRequest("device-graph-image-urls", image);
    generatePresignedUrlRequest.setMethod(HttpMethod.GET); // Default.
    generatePresignedUrlRequest.setExpiration(expiration);

    URL s = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
    return s.toString();
  }

  @Override
  public String putImageUrl(String image) throws Exception {
    java.util.Date expiration = new java.util.Date();
    long msec = expiration.getTime();
    msec += 1000 * 60 * 15; // 15 mins.
    expiration.setTime(msec);

    GeneratePresignedUrlRequest generatePresignedUrlRequest =
        new GeneratePresignedUrlRequest("device-graph-image-urls", image);
    generatePresignedUrlRequest.setMethod(HttpMethod.PUT); // Default.
    generatePresignedUrlRequest.setExpiration(expiration);

    URL s = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
    return s.toString();
  }

  @PostConstruct
  public void init() {
    s3Client =  AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_2).build();

  }
}
