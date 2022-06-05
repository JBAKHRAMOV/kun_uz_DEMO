package com.company.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentRequestDTO {
    @NotNull
    private String content;
    @NotNull
    private Integer profileId;
    @NotNull
    private Integer articleId;
}
