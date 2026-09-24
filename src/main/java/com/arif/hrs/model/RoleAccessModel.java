package com.arif.hrs.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "roleAccess")
@Table(name = "sys_role_access", uniqueConstraints = @UniqueConstraint(columnNames = { "sys_role_id",
    "sys_access_id" }))
@Getter
@Setter
@NoArgsConstructor
public class RoleAccessModel extends BasicModel {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sys_role_id", referencedColumnName = "id")
  private RoleModel role;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sys_access_id", referencedColumnName = "id")
  private AccessModel access;

}
