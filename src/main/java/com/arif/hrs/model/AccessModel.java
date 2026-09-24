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
  private String method;

  @Column(name = "description")
  private String description;
}
