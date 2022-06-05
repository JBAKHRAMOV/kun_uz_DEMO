package com.company.dto.responce;

import com.company.dto.AttachDTO;
import com.company.enums.ProfileRole;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class ProfileResponseDTO {
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
}
