package com.arif.hrs.model;

import com.arif.hrs.model.enums.ToleranceUnitEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "shiftTolerance")
@Table(name = "r_shift_tolerance", uniqueConstraints = @UniqueConstraint(columnNames = { "code" }))
@Getter
@Setter
@NoArgsConstructor
public class ShiftToleranceModel extends BasicModel {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "code")
  private String code;

  @Column(name = "tolerance_name")
  private String toleranceName;

  @Enumerated(EnumType.STRING)
  @Column(name = "tolerance_before_unit")
  private ToleranceUnitEnum toleranceBeforeUnit;

  @Enumerated(EnumType.STRING)
  @Column(name = "tolerance_after_unit")
  private ToleranceUnitEnum toleranceAfterUnit;

  @Column(name = "tolerance_before")
  private Integer toleranceBefore;

  @Column(name = "tolerance_after")
  private Integer toleranceAfter;

}
