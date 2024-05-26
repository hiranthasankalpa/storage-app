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
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
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

  private String ip;

  public List<StoredFileDto> getAllFilesForUser(String username) {
    List<StoredFileDto> files = new ArrayList<>();

    return files;
  }

  @Override
  public StoredFileDto uploadFile(String userName, MultipartFile file, String fileName, String tags,
      String visibility) throws IOException {

    validateInputs(userName, file, fileName, tags, visibility);

    String filePath = "/storage-app/files";

    if (Visibility.PUBLIC == Visibility.valueOf(visibility)) {
      filePath += "/" + Visibility.PUBLIC;
    } else {
      filePath += "/" + Visibility.PRIVATE + "/" + userName;
    }

    File directory = new File(filePath);
    if (!directory.exists()){
      boolean done = directory.mkdirs();
      log.info(String.valueOf(done));
    }

    filePath += "/" + fileName;

    StoredFile storedFile = StoredFile.builder()
        .userName(userName)
        .fileName(fileName)
        .fileType(tika.detect(file.getInputStream()))
        .fileSize(file.getSize())
        .fileLink(filePath)
        .tags(Arrays.asList(tags.split(",", -1)))
        .visibility(Visibility.valueOf(visibility))
        .build();

    file.transferTo(new File(filePath));

    return StoredFileMapper.toStoredFileDto(storedFileRepository.save(storedFile));
  }

  public ResponseEntity<InputStreamResource> downloadFile(String id) {

    Optional<StoredFile> storedFileOptional = storedFileRepository.findById(id);

    if (!storedFileOptional.isPresent()) {
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
        .header("Content-disposition", "attachment; filename="+ storedFile.getFileName())
        .body(resource);
  }

  public StoredFileResponseDto getAllFiles(String userName) {

    if (ip == null) {
      try {
        ip = getIp();
      } catch (Exception e) {
        log.warn(e.getMessage());
        log.warn("Unable to retrieve actual IP, using localhost!");
        ip = "localhost";
      }
    }

    final String downloadPath = "http://" + ip + ":" + port + path + "/file/download"+ "/" ;

    List<StoredFile> privateFiles = storedFileRepository.findByUserNameAndVisibility(userName, Visibility.PRIVATE);
    List<StoredFile> publicFiles = storedFileRepository.findByVisibility(Visibility.PUBLIC);

    StoredFileResponseDto storedFileResponseDto = StoredFileResponseDto.builder()
        .userName(userName)
        .publicFiles(new ArrayList<>())
        .privateFiles(new ArrayList<>())
        .build();

    for (StoredFile file : privateFiles) {
      storedFileResponseDto.getPrivateFiles().add(downloadPath + file.getId());
    }

    for (StoredFile file : publicFiles) {
      storedFileResponseDto.getPublicFiles().add(downloadPath + file.getId());
    }

    return storedFileResponseDto;
  }

  private void validateInputs(String userName, MultipartFile file, String fileName, String tags,
      String visibility) {

    if(StringUtils.isBlank(userName)) {
      throw new ApiException(ErrorConstants.INVALID_USER_NAME, HttpStatus.BAD_REQUEST);
    }

    if(!StringUtils.isAlphanumeric(userName)) {
      throw new ApiException(ErrorConstants.NON_ALPHANUMERIC_USER_NAME, HttpStatus.BAD_REQUEST);
    }

    if(!EnumUtils.isValidEnum(Visibility.class, visibility)) {
      throw new ApiException(ErrorConstants.VISIBILITY_ENUM_INVALID, HttpStatus.BAD_REQUEST);
    }

    if(StringUtils.isBlank(fileName)) {
      throw new ApiException(ErrorConstants.INVALID_FILE_NAME, HttpStatus.BAD_REQUEST);
    }

    if(file.isEmpty()) {
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

  private String getIp() throws SocketException, UnknownHostException {
    try (final DatagramSocket datagramSocket = new DatagramSocket()) {
      datagramSocket.connect(InetAddress.getByName("8.8.8.8"), 12345);
      return datagramSocket.getLocalAddress().getHostAddress();
    }

  }
}