package com.hirantha.storage.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

  String uploadFiles(String userName, MultipartFile file, String tags);

}
