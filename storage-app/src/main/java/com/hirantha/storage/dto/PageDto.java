package com.hirantha.storage.dto;

import com.hirantha.storage.enums.SortField;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public class PageDto {

  private int page;
  private int sizePerPage;
  private List<String> filterTags;
  private SortField sortField;
  private Sort.Direction sortDirection;
}
