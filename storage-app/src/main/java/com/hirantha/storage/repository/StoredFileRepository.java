package com.hirantha.storage.repository;

import com.hirantha.storage.enums.Visibility;
import com.hirantha.storage.model.StoredFile;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StoredFileRepository extends MongoRepository<StoredFile, String> {

  List<StoredFile> findByFileNameIgnoreCase(String fileName);

  List<StoredFile> findByFileSize(long size);

  List<StoredFile> findByUserNameAndVisibility(String userName, Visibility visibility);

  List<StoredFile> findByVisibility(Visibility visibility);

  List<StoredFile> findByUserNameAndVisibilityAndTagsIn(String userName, Visibility visibility,
      List<String> tags);

  List<StoredFile> findByVisibilityAndTagsIn(Visibility visibility, List<String> tags);

}
