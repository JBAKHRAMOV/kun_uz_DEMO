package com.company.dto.responce;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentResponseDTO {
    private Integer id;
    private String content;
    private Integer profileId;
    private Integer articleId;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
