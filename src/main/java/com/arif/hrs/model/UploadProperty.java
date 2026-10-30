package com.arif.hrs.model;

import com.arif.hrs.model.enums.UploadStatusEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class UploadProperty {

  @Column(name = "uploadstatus")
  @Enumerated(EnumType.STRING)
  private UploadStatusEnum uploadStatus;

  @Column(name = "errormessage")
  private String errorMessage;

}
