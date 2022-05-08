package com.company.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryDTO {
    private Integer id;
    @NotNull
    private String key;
    @NotNull
    private String name;
    @NotNull
    private String nameUz;
    @NotNull
    private String nameRu;
    @NotNull
    private String nameEn;
    private Integer profileId;
    private LocalDateTime createdDate;

}
