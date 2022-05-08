package com.company.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@ToString
public class RegistrationDTO {
    @NotNull
    private String name;
    @NotNull
    private String surname;
    @NotNull
    @Email
    private String email;
    @NotNull
    private String password;
}
