package com.company.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDTO {
    private Integer id;
    @NotNull
    private String content;
    @NotNull
    private Integer profileId;
    @NotNull
    private Integer articleId;
    @NotNull
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

}
