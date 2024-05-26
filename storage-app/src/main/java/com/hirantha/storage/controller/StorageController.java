package com.hirantha.storage.controller;

import com.hirantha.storage.dto.StoredFileDto;
import com.hirantha.storage.dto.StoredFileResponseDto;
import com.hirantha.storage.service.StorageService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class StorageController {

  private final StorageService storageService;

  @PostMapping("/upload")
  public StoredFileDto uploadFile(@RequestHeader("X-User-Name") String userName,
      @RequestPart("file") MultipartFile file, @RequestPart("file-name") String fileName,
      @RequestPart("tags") String tags, @RequestPart("visibility") String visibility)
      throws IOException {
    return storageService.uploadFile(userName, file, fileName, tags, visibility);
  }

  @GetMapping("/download/{id}")
  public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String id) {
    return storageService.downloadFile(id);
  }

  @GetMapping("/getAll")
  public StoredFileResponseDto getAllFiles(@RequestHeader("X-User-Name") String userName) {
    return storageService.getAllFiles(userName);
  }

}