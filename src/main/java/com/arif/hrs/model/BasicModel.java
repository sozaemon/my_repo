package com.arif.hrs.model;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
public class BasicModel {
  @Column(name = "created")
  private Timestamp created;

  @Column(name = "updated")
  private Timestamp updated;

  @Column(name = "uuid")
  @UuidGenerator
  private UUID uuid;

  @PrePersist
  protected void onCreate() {
    this.created = Timestamp.from(Instant.now());
    this.updated = Timestamp.from(Instant.now());
  }

  @PreUpdate
  protected void onUpdated() {
    this.updated = Timestamp.from(Instant.now());
  }
}
