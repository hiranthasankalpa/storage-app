package com.hirantha.storage.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder = true)
public class StoredFileResponseDto {

  String userName;

  List<String> privateFiles;

  List<String> publicFiles;

}