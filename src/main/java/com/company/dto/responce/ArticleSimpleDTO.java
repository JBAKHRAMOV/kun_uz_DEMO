package com.company.dto.responce;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleSimpleDTO {
    private Integer id;
    private String title;
    private String description;
    private LocalDateTime publishedDate;
    private String attachUrl;
}
