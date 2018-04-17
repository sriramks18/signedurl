package com.sriramks.signedurl.controller;

import com.sriramks.signedurl.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/image/{bucket}")
public class ImageController {

  @Autowired
  @Qualifier(value = "gcsImageService")
  private ImageService gcsImageService;

  @Autowired
  @Qualifier(value = "s3ImageService")
  private ImageService s3ImageService;



  @RequestMapping(method = RequestMethod.GET)
  public String getImage(@RequestParam String image, @PathVariable String bucket) throws Exception {
    if(bucket.equals("amazon"))
    return s3ImageService.getImageUrl(image);
    else
      return gcsImageService.getImageUrl(image);
  }

  @RequestMapping(method = RequestMethod.PUT)
  public String putImage(@RequestParam String image, @PathVariable String bucket) throws Exception {
    if(bucket.equals("amazon"))
      return s3ImageService.putImageUrl(image);
    else
      return gcsImageService.putImageUrl(image);
  }

  @RequestMapping(method = RequestMethod.DELETE)
  public String removeImage() {
    return "Hello Sriram!";
  }

  public ImageService getGcsImageService() {
    return gcsImageService;
  }

  public void setGcsImageService(ImageService gcsImageService) {
    this.gcsImageService = gcsImageService;
  }

  public ImageService getS3ImageService() {
    return s3ImageService;
  }

  public void setS3ImageService(ImageService s3ImageService) {
    this.s3ImageService = s3ImageService;
  }
}
