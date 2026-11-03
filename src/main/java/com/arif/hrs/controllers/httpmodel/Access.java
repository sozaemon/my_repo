package com.arif.hrs.controllers.httpmodel;

import com.arif.hrs.domain.dto.annotations.ValidationGroups;
import com.arif.hrs.model.enums.AccessMethodEnum;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Access {
  @JsonProperty("id")
  @NotNull(message = "id is required", groups = ValidationGroups.Update.class)
  private Integer id;
  @JsonProperty("name")
  @NotBlank(message = "name is required", groups = { ValidationGroups.Create.class, ValidationGroups.Update.class })
  private String name;
  @JsonProperty("path")
  @NotBlank(message = "path is required", groups = { ValidationGroups.Create.class, ValidationGroups.Update.class })
  private String path;
  @JsonProperty("method")
  @NotNull(message = "method is required", groups = { ValidationGroups.Create.class, ValidationGroups.Update.class })
  private AccessMethodEnum method;
  @JsonProperty("description")
  private String description;
}
