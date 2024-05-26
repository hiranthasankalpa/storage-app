package com.hirantha.storage.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SortField {
  FILE_NAME("fileName"),
  UPLOADED_DATE("uploadedDate"),
  TAGS("tags"),
  FILE_TYPE("fileType"),
  FILE_SIZE("fileSize");

  private final String fieldName;

}
