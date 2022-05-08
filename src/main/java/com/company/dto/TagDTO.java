package com.company.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TagDTO extends BaseDTO {
    @NotNull
    private String key;
    private String name;
    @NotNull
    private String nameUz;
    @NotNull
    private String nameRu;
    @NotNull
    private String nameEn;

    private Boolean visible;

    private Integer profileId;

}
