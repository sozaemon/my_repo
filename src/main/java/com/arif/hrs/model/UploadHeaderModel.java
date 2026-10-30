package com.arif.hrs.model;

import com.arif.hrs.model.enums.UploadHeaderStatusEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "uploadHeader")
@Table(name = "u_upload_header")
@Getter
@Setter
@NoArgsConstructor
public class UploadHeaderModel extends BasicModel {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "modulename")
  private String moduleName;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private UploadHeaderStatusEnum status;

  @Column(name = "description")
  private String description;
}
