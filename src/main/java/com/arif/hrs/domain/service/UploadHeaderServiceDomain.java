package com.arif.hrs.domain.service;

import com.arif.hrs.domain.dto.PageDto;
import com.arif.hrs.domain.dto.PaginationDto;
import com.arif.hrs.domain.dto.UploadHeaderDto;

public interface UploadHeaderServiceDomain {
  PageDto<UploadHeaderDto> paginate(PaginationDto pageRequest);
}
