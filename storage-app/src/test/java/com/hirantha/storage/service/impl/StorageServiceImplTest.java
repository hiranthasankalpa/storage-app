package com.hirantha.storage.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.hirantha.storage.constants.ErrorConstants;
import com.hirantha.storage.dto.StoredFileDto;
import com.hirantha.storage.exception.ApiException;
import com.hirantha.storage.model.StoredFile;
import com.hirantha.storage.repository.StoredFileRepository;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

class StorageServiceImplTest {

  @Mock
  private StoredFileRepository storedFileRepository;

  @InjectMocks
  private StorageServiceImpl storageService;

  @Mock
  private MultipartFile multipartFile;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    storageService = new StorageServiceImpl(storedFileRepository);
  }


  @Test
  void uploadFile_success() throws IOException {

    String userName = "testUser";
    String fileName = "testFile.txt";
    String tags = "tag1,tag2";
    String visibility = "PUBLIC";

    when(multipartFile.getInputStream()).thenReturn(mock(FileInputStream.class));
    when(multipartFile.getSize()).thenReturn(1024L);
    when(storedFileRepository.save(any(StoredFile.class))).thenAnswer(i -> i.getArguments()[0]);
    when(storedFileRepository.findByFileNameIgnoreCase(fileName)).thenReturn(
        Collections.emptyList());
    when(storedFileRepository.findByFileSize(multipartFile.getSize())).thenReturn(
        Collections.emptyList());

    StoredFileDto result = storageService.uploadFile(userName, multipartFile, fileName, tags,
        visibility);

    assertNotNull(result);
    assertEquals(fileName, result.getFileName());
  }

  @Test
  void renameFile_success() {

    String userName = "testUser";
    String fileId = "fileId";
    String newFile = "newFile.txt";

    StoredFile storedFile = StoredFile.builder().id(fileId).userName(userName)
        .fileName("oldFile.txt").build();

    when(storedFileRepository.findById(fileId)).thenReturn(Optional.of(storedFile));
    when(storedFileRepository.findByFileNameIgnoreCase(newFile)).thenReturn(
        Collections.emptyList());
    when(storedFileRepository.save(any(StoredFile.class))).thenAnswer(i -> i.getArguments()[0]);

    StoredFileDto result = storageService.renameFile(userName, fileId, newFile);

    assertNotNull(result);
    assertEquals(newFile, result.getFileName());
  }

  @Test
  void uploadFile_invalidUserName() {

    String userName = "";
    String fileName = "testFile.txt";
    String tags = "tag1,tag2";
    String visibility = "PUBLIC";

    ApiException exception = assertThrows(ApiException.class,
        () -> storageService.uploadFile(userName, multipartFile, fileName, tags, visibility));
    assertEquals(ErrorConstants.INVALID_USER_NAME, exception.getMessage());
  }

  @Test
  void uploadFile_invalidVisibility() {

    String userName = "testUser";
    String fileName = "testFile.txt";
    String tags = "tag1,tag2";
    String visibility = "INVALID_VISIBILITY";

    ApiException exception = assertThrows(ApiException.class,
        () -> storageService.uploadFile(userName, multipartFile, fileName, tags, visibility));
    assertEquals(ErrorConstants.VISIBILITY_ENUM_INVALID, exception.getMessage());
  }

  @Test
  void uploadFile_tooManyTags() {

    String userName = "testUser";
    String fileName = "testFile.txt";
    String tags = "tag1,tag2,tag3,tag4,tag5,tag6";
    String visibility = "PUBLIC";

    ApiException exception = assertThrows(ApiException.class,
        () -> storageService.uploadFile(userName, multipartFile, fileName, tags, visibility));
    assertEquals(ErrorConstants.TOO_MANY_TAGS, exception.getMessage());
  }

  @Test
  void uploadFile_emptyFileName() {

    String userName = "testUser";
    String fileName = "";
    String tags = "tag1,tag2";
    String visibility = "PUBLIC";

    ApiException exception = assertThrows(ApiException.class,
        () -> storageService.uploadFile(userName, multipartFile, fileName, tags, visibility));
    assertEquals(ErrorConstants.INVALID_FILE_NAME, exception.getMessage());
  }

  @Test
  void uploadFile_emptyFile() {

    String userName = "testUser";
    String fileName = "testFile.txt";
    String tags = "tag1,tag2";
    String visibility = "PUBLIC";

    when(multipartFile.isEmpty()).thenReturn(true);

    ApiException exception = assertThrows(ApiException.class,
        () -> storageService.uploadFile(userName, multipartFile, fileName, tags, visibility));
    assertEquals(ErrorConstants.EMPTY_FILE, exception.getMessage());
  }

  @Test
  void renameFile_fileNotFound() {

    String userName = "testUser";
    String fileId = "fileId";
    String newFile = "newFile.txt";

    when(storedFileRepository.findById(fileId)).thenReturn(Optional.empty());

    ApiException exception = assertThrows(ApiException.class,
        () -> storageService.renameFile(userName, fileId, newFile));
    assertEquals(ErrorConstants.NO_SUCH_FILE, exception.getMessage());
  }

  @Test
  void renameFile_noAccess() {

    String userName = "testUser";
    String fileId = "fileId";
    String newFile = "newFile.txt";

    StoredFile storedFile = StoredFile.builder().id(fileId).userName("anotherUser")
        .fileName("oldFile.txt").build();

    when(storedFileRepository.findById(fileId)).thenReturn(Optional.of(storedFile));

    ApiException exception = assertThrows(ApiException.class,
        () -> storageService.renameFile(userName, fileId, newFile));
    assertEquals(ErrorConstants.NO_ACCESS_TO_FILE, exception.getMessage());
  }

  @Test
  void deleteFile_fileNotFound() {

    String userName = "testUser";
    String fileId = "fileId";

    when(storedFileRepository.findById(fileId)).thenReturn(Optional.empty());

    ApiException exception = assertThrows(ApiException.class,
        () -> storageService.deleteFile(userName, fileId));
    assertEquals(ErrorConstants.NO_SUCH_FILE, exception.getMessage());
  }

  @Test
  void deleteFile_noAccess() {

    String userName = "testUser";
    String fileId = "fileId";

    StoredFile storedFile = StoredFile.builder().id(fileId).userName("anotherUser")
        .fileLink("/path/to/file.txt").build();

    when(storedFileRepository.findById(fileId)).thenReturn(Optional.of(storedFile));

    ApiException exception = assertThrows(ApiException.class,
        () -> storageService.deleteFile(userName, fileId));
    assertEquals(ErrorConstants.NO_ACCESS_TO_FILE, exception.getMessage());
  }

  @Test
  void deleteFile_fileDeletionFailed() {

    String userName = "testUser";
    String fileId = "fileId";

    StoredFile storedFile = StoredFile.builder().id(fileId).userName(userName)
        .fileLink("/path/to/file.txt").build();

    when(storedFileRepository.findById(fileId)).thenReturn(Optional.of(storedFile));

    File file = mock(File.class);
    when(file.delete()).thenReturn(false);

    ApiException exception = assertThrows(ApiException.class,
        () -> storageService.deleteFile(userName, fileId));
    assertEquals(ErrorConstants.ERROR_DELETING_FILE, exception.getMessage());
  }

  @Test
  void downloadFile_fileNotFound() {

    String fileId = "fileId";

    when(storedFileRepository.findById(fileId)).thenReturn(Optional.empty());

    ApiException exception = assertThrows(ApiException.class,
        () -> storageService.downloadFile(fileId));
    assertEquals(ErrorConstants.NO_SUCH_FILE, exception.getMessage());
  }

  @Test
  void uploadFile_ioException() throws IOException {

    String userName = "testUser";
    String fileName = "testFile.txt";
    String tags = "tag1,tag2";
    String visibility = "PUBLIC";
    String filePath = "/storage-app/files/PUBLIC/testFile.txt";

    File directory = mock(File.class);
    when(directory.exists()).thenReturn(true);
    when(multipartFile.getInputStream()).thenThrow(IOException.class);

    ApiException exception = assertThrows(ApiException.class,
        () -> storageService.uploadFile(userName, multipartFile, fileName, tags, visibility));
    assertEquals(ErrorConstants.ERROR_CREATING_FILE, exception.getMessage());
  }

  @Test
  void deleteFile_fileDeletionError() {

    String userName = "testUser";
    String fileId = "fileId";
    StoredFile storedFile = StoredFile.builder().id(fileId).userName(userName)
        .fileLink("/path/to/file.txt").build();

    when(storedFileRepository.findById(fileId)).thenReturn(Optional.of(storedFile));

    File file = mock(File.class);
    when(file.exists()).thenReturn(true);
    when(file.delete()).thenReturn(false);

    ApiException exception = assertThrows(ApiException.class,
        () -> storageService.deleteFile(userName, fileId));
    assertEquals(ErrorConstants.ERROR_DELETING_FILE, exception.getMessage());
  }

}