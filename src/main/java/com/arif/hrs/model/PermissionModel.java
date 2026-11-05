package com.arif.hrs.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "permission")
@Table(name = "sys_permission", uniqueConstraints = @UniqueConstraint(columnNames = { "role_code",
    "permission" }))
@Getter
@Setter
@NoArgsConstructor
public class PermissionModel extends BasicModel {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "role_code", nullable = false)
  private String roleCode;

  @Column(name = "permission", nullable = false)
  private String permission;

}
