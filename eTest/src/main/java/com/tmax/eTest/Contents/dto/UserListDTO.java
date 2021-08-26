package com.tmax.eTest.Contents.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class UserListDTO {
  @NotEmpty
  private List<String> userIds;
}
