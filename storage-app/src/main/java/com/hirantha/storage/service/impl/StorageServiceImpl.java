package com.hirantha.storage.service.impl;

import com.hirantha.storage.constants.ErrorConstants;
import com.hirantha.storage.dto.StoredFileDto;
import com.hirantha.storage.dto.StoredFileResponseDto;
import com.hirantha.storage.enums.Visibility;
import com.hirantha.storage.exception.ApiException;
import com.hirantha.storage.mapper.StoredFileMapper;
import com.hirantha.storage.model.StoredFile;
import com.hirantha.storage.repository.StoredFileRepository;
import com.hirantha.storage.service.StorageService;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

  private final StoredFileRepository storedFileRepository;

  private final Tika tika = new Tika();

  @Value("${server.port}")
  private int port;

  @Value("${server.servlet.context-path}")
  private String path;

  @Override
  public StoredFileDto uploadFile(String userName, MultipartFile file, String fileName, String tags,
      String visibility) {

    validateInputs(userName, file, fileName, tags, visibility);

    String filePath = "/storage-app/files";

    if (Visibility.PUBLIC == Visibility.valueOf(visibility)) {
      filePath += "/" + Visibility.PUBLIC;
    } else {
      filePath += "/" + Visibility.PRIVATE + "/" + userName;
    }

    File directory = new File(filePath);
    if (!directory.exists()) {
      boolean done = directory.mkdirs();
      if (!done) {
        log.error("Directory could not be created");
        throw new ApiException(ErrorConstants.ERROR_CREATING_FILE,
            HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    filePath += "/" + fileName;

    StoredFile storedFile;
    try {
      storedFile = StoredFile.builder()
          .userName(userName)
          .fileName(fileName)
          .fileType(tika.detect(file.getInputStream()))
          .fileSize(file.getSize())
          .fileLink(filePath)
          .tags(Arrays.asList(tags.split(",", -1)))
          .visibility(Visibility.valueOf(visibility))
          .build();

      file.transferTo(new File(filePath));
    } catch (IOException ex) {
      log.error("File could not be created");
      throw new ApiException(ErrorConstants.ERROR_CREATING_FILE, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    return StoredFileMapper.toStoredFileDto(storedFileRepository.save(storedFile));
  }

  @Override
  public ResponseEntity<InputStreamResource> downloadFile(String id) {

    Optional<StoredFile> storedFileOptional = storedFileRepository.findById(id);

    if (storedFileOptional.isEmpty()) {
      throw new ApiException(ErrorConstants.NO_SUCH_FILE, HttpStatus.BAD_REQUEST);
    }

    StoredFile storedFile = storedFileOptional.get();
    InputStreamResource resource;
    try {
      resource = new InputStreamResource(new FileInputStream(storedFile.getFileLink()));
    } catch (FileNotFoundException e) {
      throw new ApiException(ErrorConstants.FILE_NOT_FOUND, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .header("Content-disposition", "attachment; filename=" + storedFile.getFileName())
        .body(resource);
  }

  @Override
  public StoredFileResponseDto listFiles(String userName) {
    final String downloadPath = "http://localhost:" + port + path + "/files/download" + "/";

    List<StoredFile> privateFiles = storedFileRepository.findByUserNameAndVisibility(userName,
        Visibility.PRIVATE);
    List<StoredFile> publicFiles = storedFileRepository.findByVisibility(Visibility.PUBLIC);

    return StoredFileResponseDto.builder()
        .userName(userName)
        .publicFiles(StoredFileMapper.toStoredFileList(publicFiles, downloadPath))
        .privateFiles(StoredFileMapper.toStoredFileList(privateFiles, downloadPath))
        .build();
  }

  private void validateInputs(String userName, MultipartFile file, String fileName, String tags,
      String visibility) {

    if (StringUtils.isBlank(userName)) {
      throw new ApiException(ErrorConstants.INVALID_USER_NAME, HttpStatus.BAD_REQUEST);
    }

    if (!StringUtils.isAlphanumeric(userName)) {
      throw new ApiException(ErrorConstants.NON_ALPHANUMERIC_USER_NAME, HttpStatus.BAD_REQUEST);
    }

    if (!EnumUtils.isValidEnum(Visibility.class, visibility)) {
      throw new ApiException(ErrorConstants.VISIBILITY_ENUM_INVALID, HttpStatus.BAD_REQUEST);
    }

    if (StringUtils.isBlank(fileName)) {
      throw new ApiException(ErrorConstants.INVALID_FILE_NAME, HttpStatus.BAD_REQUEST);
    }

    if (file.isEmpty()) {
      throw new ApiException(ErrorConstants.EMPTY_FILE, HttpStatus.BAD_REQUEST);
    }

    if (!storedFileRepository.findByFileNameIgnoreCase(fileName).isEmpty()) {
      throw new ApiException(ErrorConstants.FILE_NAME_EXISTS, HttpStatus.BAD_REQUEST);
    }

    if (!storedFileRepository.findByFileSize(file.getSize()).isEmpty()) {
      // TODO: check files byte by byte
      throw new ApiException(ErrorConstants.FILE_CONTENT_EXISTS, HttpStatus.BAD_REQUEST);
    }
  }

}
