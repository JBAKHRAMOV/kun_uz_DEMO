package com.company.dto.request;

import com.company.enums.LikeStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class LikeRequestDTO {
    @NotNull
    private LikeStatus status;
    @NotNull
    private Integer articleId;
}
