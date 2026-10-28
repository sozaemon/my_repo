package com.arif.hrs.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.arif.hrs.domain.dto.PageDto;
import com.arif.hrs.domain.dto.PaginationDto;
import com.arif.hrs.domain.dto.UploadHeaderDto;
import com.arif.hrs.domain.service.UploadHeaderServiceDomain;
import com.arif.hrs.mapper.UploadHeaderMapper;
import com.arif.hrs.model.UploadHeaderModel;
import com.arif.hrs.repository.UploadHeaderRepository;
import com.arif.hrs.service.serviceutility.CommonImpl;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UploadHeaderServiceImpl implements UploadHeaderServiceDomain {
  private final UploadHeaderRepository uploadHeaderRepository;
  private final UploadHeaderMapper mapper;

  public UploadHeaderServiceImpl(UploadHeaderRepository uploadHeaderRepository, UploadHeaderMapper mapper) {
    this.uploadHeaderRepository = uploadHeaderRepository;
    this.mapper = mapper;
  }

  @Override
  public PageDto<UploadHeaderDto> paginate(PaginationDto pageRequest) {
    log.info("Paginate Upload Header");

    Specification<UploadHeaderModel> specification = CommonImpl
        .buildFilterSpecificationDto(pageRequest.getFilters(), UploadHeaderDto.class);

    Pageable page = CommonImpl.buildPaginationPage(pageRequest, "id", UploadHeaderDto.class);

    Page<UploadHeaderModel> pageModel = uploadHeaderRepository.findAll(specification, page);
    List<UploadHeaderDto> dtoData = pageModel.getContent().stream().map(mapper::modelToDto).toList();

    return new PageDto<>(pageModel, dtoData);
  }

}
