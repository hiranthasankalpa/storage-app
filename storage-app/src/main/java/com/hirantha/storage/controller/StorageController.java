package com.hirantha.storage.controller;

import com.hirantha.storage.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class StorageController {

  private final StorageService storageService;

  @RequestMapping(
      path = "/upload",
      method = RequestMethod.POST,
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @PostMapping("/upload")
  public String uploadFiles(@RequestHeader("X-User-Name") String userName,
      @RequestPart("file") MultipartFile file,
      @RequestPart("tags") String tags) {
    return storageService.uploadFiles(userName, file, tags);
  }

  @GetMapping("/getAll")
  public String getAllFiles(@RequestHeader("X-User-Name") String userName) {
    return userName;
  }

}
