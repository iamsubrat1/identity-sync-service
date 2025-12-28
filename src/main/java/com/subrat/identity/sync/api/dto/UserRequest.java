package com.subrat.identity.sync.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String status;
}
