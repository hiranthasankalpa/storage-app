package com.hirantha.storage.service.impl;

import com.hirantha.storage.service.StorageService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
//@RequiredArgsConstructor
//@NoArgsConstructor(force = true)
public class StorageServiceImpl implements StorageService {

  @Override
  public String uploadFiles(String userName, MultipartFile file, String tags) {
    return userName;
  }
}
