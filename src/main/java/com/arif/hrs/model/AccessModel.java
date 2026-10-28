package com.arif.hrs.model;

import java.util.HashSet;
import java.util.Set;

import com.arif.hrs.model.enums.AccessMethodEnum;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "accessModel")
@Table(name = "sys_access", uniqueConstraints = @UniqueConstraint(columnNames = { "path", "method" }))
@Getter
@Setter
@NoArgsConstructor
public class AccessModel extends BasicModel {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "name")
  private String name;

  @Column(name = "path")
  private String path;

  @Column(name = "method")
  @Enumerated(EnumType.STRING)
  private AccessMethodEnum method;

  @Column(name = "description")
  private String description;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "access", cascade = CascadeType.ALL)
  private Set<RoleAccessModel> roleAccess = new HashSet<>();
}
