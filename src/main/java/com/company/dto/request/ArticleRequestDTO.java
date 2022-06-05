package com.company.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

@Setter
@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleRequestDTO {
    @NotNull
    private String title;
    @NotNull
    private String description;
    @NotNull
    private String content;
    @NotNull
    private String attachId;
    @NotNull
    private Integer categoryId;
    @NotNull
    private Integer regionId;
    @NotNull
    private Integer typeId;
    @NotNull
    private List<Integer> tagIdList; // create
}
