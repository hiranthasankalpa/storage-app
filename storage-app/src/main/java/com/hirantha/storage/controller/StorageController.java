package com.hirantha.storage.controller;

import com.hirantha.storage.dto.PageDto;
import com.hirantha.storage.dto.StoredFileDto;
import com.hirantha.storage.dto.StoredFileResponseDto;
import com.hirantha.storage.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class StorageController {

  private final StorageService storageService;

  /**
   * This method allows the user to upload files. With a filename, visibility setting
   * (PUBLIC/PRIVATE) and set of tags (up to 5 per file) without limit on the file size
   *
   * @param userName   alphanumeric userName for identifying file ownership alphanumeric userName
   *                   for identifying file ownership
   * @param file       multipart file being uploaded
   * @param fileName   name of the file user provided to save the file under
   * @param tags       up to five tags accepted as comma seperated string
   * @param visibility PRIVATE or PUBLIC based on visibility user chooses for the file
   * @return StoredFileDto which contains details of the created file
   */
  @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public StoredFileDto uploadFile(@RequestHeader("X-User-Name") String userName,
      @RequestPart("file") MultipartFile file, @RequestPart("file-name") String fileName,
      @RequestPart("tags") String tags, @RequestPart("visibility") String visibility) {
    return storageService.uploadFile(userName, file, fileName, tags, visibility);
  }

  /**
   * This method allows the user to change the filename of files uploaded.
   *
   * @param userName      alphanumeric userName for identifying file ownership
   * @param storedFileDto used to get the id of the file user wants to rename and the new name
   * @return StoredFileDto which contains details of the updated file
   */
  @PutMapping("/rename")
  public StoredFileDto renameFile(@RequestHeader("X-User-Name") String userName,
      @RequestBody StoredFileDto storedFileDto) {
    return storageService.renameFile(userName, storedFileDto.getId(), storedFileDto.getFileName());
  }

  /**
   * This method allows the user to list files stored in the application. Which are PUBLIC or
   * belonging to the user Private. All file lists can be filtered by TAGs and be sortable by the
   * FILE_NAME, UPLOAD_DATE, TAGS, FILE_TYPE and FILE_SIZE, and should be divided into result pages
   * containing defined amount of results. After successful upload, user is provided with a download
   * link for the file The application prevents users from uploading the same file multiple times,
   * based on content or filename.
   *
   * @param userName alphanumeric userName for identifying file ownership
   * @param pageDto  containing paging, filtering and sorting parameters, has defaults set in dto
   * @return StoredFileResponseDto contains list of private and public files which are sorted,
   * filtered and paged, also contains total amount of results for each
   */
  @PostMapping("/list")
  public StoredFileResponseDto listFiles(@RequestHeader("X-User-Name") String userName,
      @RequestBody(required = false) PageDto pageDto) {
    return storageService.listFiles(userName, pageDto);
  }

  /**
   * This method allows the user to delete files that they uploaded.
   *
   * @param userName alphanumeric userName for identifying file ownership
   * @param id       of the file to be deleted
   * @return String which confirms the deletion of the file
   */
  @DeleteMapping("/delete/{id}")
  public String deleteFile(@RequestHeader("X-User-Name") String userName,
      @PathVariable String id) {
    return storageService.deleteFile(userName, id);
  }

  /**
   * All files (both PRIVATE and PUBLIC) can be downloaded using this method by the file ID.
   *
   * @param id of the file to be downloaded
   * @return the file to download as octet-stream media type
   */
  @GetMapping("/download/{id}")
  public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String id) {
    return storageService.downloadFile(id);
  }

}
