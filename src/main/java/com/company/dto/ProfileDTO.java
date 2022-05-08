package com.company.dto;

import com.company.enums.ProfileRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileDTO {
    private Integer id;
    @NotNull
    @Size(min = 3, max = 15, message = "name size min=3 , max=15")
    private String name;
    @NotNull
    @Size(min = 3, max = 15, message = "surname size min=3 , max=15")
    private String surname;
    @Email
    private String email;
    @NotNull
    @Size(min = 3, max = 15, message = "password size min=3 , max=15")
    private String password;
    private ProfileRole role;
    private LocalDateTime updatedDate;
    private LocalDateTime createdDate;

    private String jwt;

    private AttachDTO image;
}
