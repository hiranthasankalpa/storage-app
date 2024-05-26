package com.hirantha.storage.model;

import com.hirantha.storage.enums.Visibility;
import java.util.Date;
import java.util.List;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder(toBuilder = true)
@Document(collection = "stored_files")
public class StoredFile {

  @Id
  private String id;

  private String userName;

  private String fileName;

  private String fileType;

  private long fileSize;

  private String fileLink;

  private List<String> tags;

  @Enumerated(EnumType.STRING)
  private Visibility visibility;

  @CreatedDate
  private Date uplodedDate;

  @LastModifiedDate
  private Date modifiedDate;

  @Override
  public String toString() {
    return String.format(
        "StoredFile[id=%s, userName='%s', fileName='%s', visibility='%s']",
        id, userName, fileName, visibility);
  }

}
