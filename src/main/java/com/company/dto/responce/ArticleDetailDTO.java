package com.company.dto.responce;

import com.company.dto.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
@Data
public class ArticleDetailDTO {
    private Integer id;
    private String title;
    private String description;
    private String content;
    private Integer profileId;
    private LocalDateTime updatedDate;
    private LocalDateTime createdDate;
    private LocalDateTime publishedDate;
    private String attachURL;
    private Integer viewCount;
    private LikeCountDTO like;
    private Integer sharedCount;
    private CategoryResponseDTO category;
    private RegionDTO region;
    private Integer regionId;
    private Integer typeId;
    private ArticleTypeResponseDTO articleType;
    private List<TagDTO> tagList;

    public ArticleDetailDTO(ArticleResponseDTO dto) {
        this.id = dto.getId();
        this.title = dto.getTitle();
        this.description = dto.getDescription();
        this.content = dto.getContent();
        this.profileId = dto.getProfileId();
        this.updatedDate = dto.getUpdatedDate();
        this.createdDate = dto.getCreatedDate();
        this.publishedDate = dto.getPublishedDate();
        this.attachURL = dto.getAttachUrl();
    }
}
