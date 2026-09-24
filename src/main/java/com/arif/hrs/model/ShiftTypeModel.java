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

@Table(name = "r_shift_type", uniqueConstraints = @UniqueConstraint(columnNames = { "code" }))
@Entity(name = "shiftType")
@Getter
@Setter
@NoArgsConstructor
public class ShiftTypeModel extends BasicModel {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "code")
  private String code;

  @Column(name = "shift_type_name")
  private String name;

}
