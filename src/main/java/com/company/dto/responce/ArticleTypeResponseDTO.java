package com.company.dto.responce;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleTypeResponseDTO {
    //id, name, uz, ru, en, key, pId, crd
    private Integer id;
    private String name;
    private String nameUz;
    private String nameRu;
    private String nameEn;
    private String key;
    private Integer profileId;
    private LocalDateTime createdDate;
}
