package com.arif.hrs.model;

import com.arif.hrs.model.enums.AccessMethodEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "roleAccessUpload")
@Table(name = "u_role_access")
@Getter
@Setter
@NoArgsConstructor
public class RoleAccessUploadModel extends BasicModel {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "name")
  private String name;

  @Column(name = "path")
  private String path;

  @Column(name = "method")
  private AccessMethodEnum method;

  @Column(name = "description")
  private String description;

  @Column(name = "roles")
  private String roles;

  @Embedded
  private UploadProperty uploadProperty;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "upload_header_id", referencedColumnName = "id")
  private UploadHeaderModel uploadHeader;

}
