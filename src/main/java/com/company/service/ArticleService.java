package com.company.service;

import com.company.dto.ArticleDTO;
import com.company.entity.ArticleEntity;
import com.company.entity.ProfileEntity;
import com.company.enums.ArticleStatus;
import com.company.enums.LangEnum;
import com.company.exp.ItemAlreadyExistsException;
import com.company.exp.ItemNotFoundException;
import com.company.mapper.ArticleSimpleMapper;
import com.company.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleService {
    @Value("${server.domain.name}")
    private String domainName;

    private final   ArticleRepository articleRepository;
    private final ProfileService profileService;
    private final AttachService attachService;
    private final LikeService likeService;
    private final CategoryService categoryService;
    private final RegionService regionService;
    private final ArticleTypeService articleTypeService;
    private final TagService tagService;


    public ArticleDTO create(ArticleDTO dto, Integer pId) {

        Optional<ArticleEntity> optional = articleRepository.findByTitle(dto.getTitle());
        if (optional.isPresent()) {
            log.warn("This Article already used! : {}", dto );
            throw new ItemAlreadyExistsException("This Article already used!");
        }

        ArticleEntity entity = new ArticleEntity();
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setContent(dto.getContent());
        entity.setProfileId(pId);

        entity.setStatus(ArticleStatus.CREATED);

        entity.setAttachId(dto.getAttachId());
        entity.setCategoryId(dto.getCategoryId());
        entity.setRegionId(dto.getRegionId());
        entity.setTypeId(dto.getTypeId());
        entity.setTagList(dto.getTagIdList());

        articleRepository.save(entity);
        return toDTO(entity);
    }

    public List<ArticleDTO> list(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));

        return articleRepository.findByVisible(true, pageable)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public Boolean update(Integer id, ArticleDTO dto, Integer pId) {
        ProfileEntity profile = profileService.get(pId);

        Integer aId = getOrCheckByVisable(id).getId();

        return 0 < articleRepository.updateDetail(dto.getTitle(), dto.getDescription(), dto.getContent(), profile, LocalDateTime.now(), aId);
    }

    public Boolean delete(Integer id) {
        getOrCheckByVisable(id);

        return 0 < articleRepository.updateVisible(false, id);
    }

    public List<ArticleDTO> getTop5ByTypeId(Integer typeId) {
       return articleRepository.getTypeId(typeId, ArticleStatus.PUBLISHED.name())
                .stream()
                .map(this::toSimpleDTO)
                .toList();
    }

    public ArticleDTO getByIdPublished(Integer articleId, LangEnum lang) {
        var entity = articleRepository.findByIdAndStatus(articleId, ArticleStatus.PUBLISHED)
                .orElseThrow(()->new ItemNotFoundException("Item not found"));

        return toDetailDTO(entity, lang);
    }

    public ArticleDTO getByIdAdAdmin(Integer articleId, LangEnum lang) {
        var entity = articleRepository.findById(articleId)
                .orElseThrow(()->new ItemNotFoundException("Item not found"));

        return toDetailDTO(entity, lang);
    }

    public PageImpl<ArticleDTO> publishedListByRegion(Integer regionId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));

        var pagination=articleRepository.findByRegionIdAndStatus(regionId, pageable, ArticleStatus.PUBLISHED);

        var list= pagination
                .stream()
                .map(this::toSimpleDTO)
                .toList();

        return new PageImpl<>(list, pageable, pagination.getTotalElements());

    }

    public PageImpl<ArticleDTO> publishedListByCategoryId(int page, int size, Integer cId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));

        var pagination = articleRepository.findByCategoryIdAndStatus(cId, pageable, ArticleStatus.PUBLISHED);
        var list=pagination
                .stream()
                .map(this::toSimpleDTO)
                .toList();

        return new PageImpl<>(list, pageable, pagination.getTotalElements());
    }

    public PageImpl<ArticleDTO> publishedListByTypeId(int page, int size, Integer tId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));

        var pagination= articleRepository.findByTypeIdAndStatus(tId, pageable, ArticleStatus.PUBLISHED);

        var list=pagination
                .stream()
                .map(this::toSimpleDTO)
                .toList();

        return new PageImpl<>(list, pageable, pagination.getTotalElements());
    }

    public List<ArticleDTO> last4() {
        return articleRepository.getLast4(ArticleStatus.PUBLISHED.name())
                .stream()
                .map(this::toSimpleDTO)
                .toList();
    }

    public List<ArticleDTO> top4ByRegionId(Integer rId) {
        return articleRepository.getByRegionIdLast4(rId, ArticleStatus.PUBLISHED.name())
                .stream()
                .map(this::toSimpleDTO)
                .toList();
    }

    public List<ArticleDTO> top4ByCategoryId(Integer cId) {
        return articleRepository.getByCategoryIdLast4(cId, ArticleStatus.PUBLISHED.name())
                .stream()
                .map(this::toSimpleDTO)
                .toList();
    }

    public Boolean changeStatus(Integer aId, ArticleStatus status) {
        var articleStatus = getOrCheckByVisable(aId).getStatus();
        if (articleStatus.equals(status))
            return false;

        return articleRepository.updateStatus(status, aId) > 0;
    }

    public String getShared(LangEnum lang, Integer id) {
        getOrCheckByVisable(id);
        articleRepository.updateSharedCount( id);
        return domainName + "/article/" + lang + "/" + id;
    }

    public void updateViewCount(Integer articleId) {
        articleRepository.updateViewCount(articleId);
    }

    public ArticleDTO toDetailDTO(ArticleEntity entity, LangEnum lang) {
        ArticleDTO dto = toDTO(entity);

        dto.setViewCount(entity.getViewCount());
        dto.setSharedCount(entity.getSharedCount());

        dto.setLike(likeService.getLIkeAndDislikeCount(entity.getId()));  // like
        dto.setCategory(categoryService.getById(entity.getCategoryId(), lang)); // category
        dto.setRegion(regionService.getById(entity.getRegionId(), lang)); // region
        dto.setArticleType(articleTypeService.getById(entity.getTypeId(), lang)); // type
        dto.setTagList(tagService.getTagList(entity.getTagList(), lang)); // tag
        return dto;
    }

    private ArticleDTO toSimpleDTO(ArticleSimpleMapper entity) {
        ArticleDTO dto = new ArticleDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setPublishedDate(entity.getPublished_date());

        dto.setImage(attachService.toOpenURLDTO(entity.getAttach_id()));


        return dto;
    }

    private ArticleDTO toSimpleDTO(ArticleEntity entity) {
        ArticleDTO dto = new ArticleDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setPublishedDate(entity.getPublishedDate());

        dto.setImage(attachService.toOpenURLDTO(entity.getAttachId()));

        return dto;
    }

    private ArticleDTO toDTO(ArticleEntity entity) {
        ArticleDTO dto = new ArticleDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setContent(entity.getContent());

        dto.setProfileId(entity.getProfileId());

        dto.setUpdatedDate(entity.getUpdatedDate());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setPublishedDate(entity.getPublishedDate());

        dto.setImage(attachService.toOpenURLDTO(entity.getAttachId()));

        return dto;
    }

    public ArticleEntity checkOrGet(Integer articleId) {
        return articleRepository.findById(articleId).orElseThrow(() -> {
            throw new ItemNotFoundException("Article Not found");
        });
    }

    public ArticleEntity getOrCheckByVisable(Integer articleId) {
        var article= articleRepository.findById(articleId).orElseThrow(() -> {
            throw new ItemNotFoundException("Article Not found");
        });
        if (!article.getVisible()){
            throw new ItemNotFoundException("Article Not found");
        }
        return article;
    }
}
