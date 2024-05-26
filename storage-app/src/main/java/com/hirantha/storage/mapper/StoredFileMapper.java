package com.hirantha.storage.mapper;

import com.hirantha.storage.dto.StoredFileDto;
import com.hirantha.storage.model.StoredFile;
import java.util.List;
import org.springframework.data.domain.Page;

public class StoredFileMapper {

  public static StoredFileDto toStoredFileDto(StoredFile storedFile) {
    return StoredFileDto.builder()
        .id(storedFile.getId())
        .userName(storedFile.getUserName())
        .fileName(storedFile.getFileName())
        .fileType(storedFile.getFileType())
        .fileSize(storedFile.getFileSize())
        .fileLink(storedFile.getFileLink())
        .tags(storedFile.getTags())
        .visibility(storedFile.getVisibility())
        .uploadedDate(storedFile.getUploadedDate())
        .modifiedDate(storedFile.getModifiedDate())
        .build();
  }

  public static StoredFileDto toStoredFilesForDownload(StoredFile storedFile, String path) {
    return StoredFileDto.builder()
        .id(storedFile.getId())
        .fileName(storedFile.getFileName())
        .fileType(storedFile.getFileType())
        .fileSize(storedFile.getFileSize())
        .fileLink(path + storedFile.getId())
        .tags(storedFile.getTags())
        .visibility(storedFile.getVisibility())
        .uploadedDate(storedFile.getUploadedDate())
        .modifiedDate(storedFile.getModifiedDate())
        .build();
  }

  public static List<StoredFileDto> toStoredFileList(Page<StoredFile> storedFiles, String path) {
    return storedFiles.stream()
        .map((StoredFile storedFile) -> toStoredFilesForDownload(storedFile, path)).toList();
  }

}