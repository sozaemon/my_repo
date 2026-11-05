package com.arif.hrs.controllers.httpmodel;

import com.arif.hrs.domain.dto.annotations.ValidationGroups;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Permission {
  @JsonProperty("id")
  @NotNull(message = "id is required", groups = ValidationGroups.Update.class)
  private Integer id;
  @JsonProperty("roleCode")
  @NotBlank(message = "role code is required", groups = { ValidationGroups.Create.class,
      ValidationGroups.Update.class })
  private String roleCode;
  @JsonProperty("permission")
  @NotBlank(message = "permission is required", groups = { ValidationGroups.Create.class,
      ValidationGroups.Update.class })
  private String permission;
}
