package com.company.dto.request;

import com.company.enums.LikeStatus;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class LikeDetailDTO {
    @NotNull
    private LikeStatus status;
}
