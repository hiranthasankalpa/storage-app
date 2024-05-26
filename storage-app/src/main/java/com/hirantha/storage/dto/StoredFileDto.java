package com.hirantha.storage.dto;

import com.hirantha.storage.enums.Visibility;
import java.util.Date;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder = true)
public class StoredFileDto {

  private String id;

  private String userName;

  private String fileName;

  private String fileType;

  private long fileSize;

  private String fileLink;

  private List<String> tags;

  private Visibility visibility;

  private Date uplodedDate;

  private Date modifiedDate;

}