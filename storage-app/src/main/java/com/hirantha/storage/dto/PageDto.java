package com.hirantha.storage.dto;

import com.hirantha.storage.enums.SortField;
import java.util.List;
import lombok.Data;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

@Data
public class PageDto {

  private int page = 0;
  private int sizePerPage = 5;
  private List<String> filterTags;
  private SortField sortField = SortField.UPLOADED_DATE;
  private Sort.Direction sortDirection = Direction.DESC;

}
