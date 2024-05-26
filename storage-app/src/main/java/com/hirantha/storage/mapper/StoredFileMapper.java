package com.hirantha.storage.mapper;

import com.hirantha.storage.dto.StoredFileDto;
import com.hirantha.storage.model.StoredFile;

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
        .uplodedDate(storedFile.getUplodedDate())
        .modifiedDate(storedFile.getModifiedDate())
        .build();
  }

//  public static StoredFile toStoredFile(StoredFileDto storedFileDto) {
//    return StoredFile.builder()
//        .id(storedFileDto.getId())
//        .userName(storedFileDto.getUserName())
//        .fileName(storedFileDto.getFileName())
//        .fileType(storedFileDto.getFileType())
//        .fileSize(storedFileDto.getFileSize())
//        .fileLink(storedFileDto.getFileLink())
//        .tags(storedFileDto.getTags())
//        .visibility(storedFileDto.getVisibility())
//        .uplodedDate(storedFileDto.getUplodedDate())
//        .modifiedDate(storedFileDto.getModifiedDate())
//        .build();
//  }
}