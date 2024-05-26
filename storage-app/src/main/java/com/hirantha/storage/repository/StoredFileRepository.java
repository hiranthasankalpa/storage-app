package com.hirantha.storage.repository;

import com.hirantha.storage.enums.Visibility;
import com.hirantha.storage.model.StoredFile;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StoredFileRepository extends MongoRepository<StoredFile, String> {

  List<StoredFile> findByFileNameIgnoreCase(String fileName);

  List<StoredFile> findByFileSize(long size);

  Page<StoredFile> findByUserNameAndVisibility(String userName, Visibility visibility, Pageable pageable);

  Page<StoredFile> findByVisibility(Visibility visibility, Pageable pageable);

  Page<StoredFile> findByUserNameAndVisibilityAndTagsIn(String userName, Visibility visibility,
      List<String> tags, Pageable pageable);

  Page<StoredFile> findByVisibilityAndTagsIn(Visibility visibility, List<String> tags, Pageable pageable);

}
