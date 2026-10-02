package com.arif.hrs.model;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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

  @Column(name = "createdby")
  private String createdBy;

  @Column(name = "updatedBy")
  private String updatedBy;

  @Column(name = "uuid")
  @UuidGenerator
  private UUID uuid;

  @PrePersist
  protected void onCreate() {

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String userName = auth.getName();

    setCreated(Timestamp.from(Instant.now()));
    setUpdated(Timestamp.from(Instant.now()));
    setCreatedBy(userName);
    setUpdatedBy(userName);

  }

  @PreUpdate
  protected void onUpdated() {

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String userName = auth.getName();

    setUpdated(Timestamp.from(Instant.now()));
    setUpdatedBy(userName);
  }
}
