package com.arif.hrs.controllers.httpmodel;

import com.arif.hrs.domain.dto.annotations.ValidationGroups;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Role {
  @JsonProperty("id")
  @NotNull(message = "id is required", groups = ValidationGroups.Update.class)
  private Integer id;
  @JsonProperty("code")
  @NotBlank(message = "code is required", groups = { ValidationGroups.Create.class, ValidationGroups.Update.class })
  private String code;
  @JsonProperty("name")
  @NotBlank(message = "name is required", groups = { ValidationGroups.Create.class, ValidationGroups.Update.class })
  private String name;
  @JsonProperty("description")
  private String description;
  @JsonProperty("active")
  private Boolean active;

}
