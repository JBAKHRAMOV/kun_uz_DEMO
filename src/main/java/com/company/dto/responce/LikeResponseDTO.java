package com.company.dto.responce;

import com.company.dto.BaseDTO;
import com.company.enums.LikeStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LikeResponseDTO extends BaseDTO {
    private LikeStatus status;
    private Integer profileId;
    private Integer articleId;
}
