package com.hirantha.storage.service;

import com.hirantha.storage.dto.PageDto;
import com.hirantha.storage.dto.StoredFileDto;
import com.hirantha.storage.dto.StoredFileResponseDto;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

  StoredFileDto uploadFile(String userName, MultipartFile file, String fileName, String tags,
      String visibility);

  StoredFileDto renameFile(String userName, String id, String fileName);

  ResponseEntity<InputStreamResource> downloadFile(String id);

  String deleteFile(String userName, String id);

  StoredFileResponseDto listFiles(String userName, PageDto pageDto);

}
