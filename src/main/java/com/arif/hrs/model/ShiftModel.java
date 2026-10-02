package com.arif.hrs.model;

import java.sql.Timestamp;

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

@Entity(name="shift")
@Table(name="t_shift", uniqueConstraints = @UniqueConstraint(columnNames = {"code"}))
@Getter 
@Setter 
@NoArgsConstructor 
public class ShiftModel extends BasicModel{
  
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "code")
  private String code;

  @Column(name = "shift_name")
  private String shiftName;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "shift_type_id", referencedColumnName="id")
  private ShiftTypeModel shiftType;

  @Column(name="in_time")
  private Timestamp inTime;

  @Column(name="out_time")
  private Timestamp outTime;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "tolerance_in_id", referencedColumnName = "id")
  private ShiftToleranceModel inTolerance;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "tolerance_out_id", referencedColumnName = "id")
  private ShiftToleranceModel outTolerance;

}
