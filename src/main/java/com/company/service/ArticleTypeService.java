package com.company.service;

import com.company.dto.ArticleTypeDTO;
import com.company.entity.ArticleTypeEntity;
import com.company.enums.LangEnum;
import com.company.enums.ProfileRole;
import com.company.exp.AppBadRequestException;
import com.company.exp.AppForbiddenException;
import com.company.exp.RegionAlreadyExistsException;
import com.company.repository.ArticleTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleTypeService {
    private final ProfileService profileService;
    private final ArticleTypeRepository articleTypeRepository;

    public ArticleTypeDTO create(ArticleTypeDTO dto, Integer pId) {

        if (!profileService.get(pId).equals(ProfileRole.ADMIN)) {
            log.warn("not acsess : {}", pId );
            throw new AppForbiddenException("Not access");
        }

        if (articleTypeRepository.findByKey(dto.getKey()) != null) {
            log.warn("Region Alredy Exists : {}", dto );
            throw new RegionAlreadyExistsException("Region Alredy Exists");
        }

        ArticleTypeEntity entity = new ArticleTypeEntity();
        entity.setNameUz(dto.getNameUz());
        entity.setNameEn(dto.getNameEn());
        entity.setNameRu(dto.getNameRu());
        entity.setKey(dto.getKey());
        entity.setProfileId(pId);

        articleTypeRepository.save(entity);

        dto.setId(entity.getId());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    public PageImpl<ArticleTypeDTO> getList(int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        var pagination = articleTypeRepository.findAll(pageable);

        var list = pagination.stream().map(this::toDTO).toList();

        return new PageImpl<>(list, pageable, pagination.getTotalElements());
    }

    public ArticleTypeDTO getById(Integer id) {
        return toDTO(checkOrGet(id));
    }

    public ArticleTypeDTO getById(Integer id, LangEnum lang) {
        return toDTO(checkOrGet(id), lang);
    }

    public String update(Integer id, ArticleTypeDTO dto) {
        if (!profileService.get(dto.getProfileId()).getRole().equals(ProfileRole.ADMIN)) {
            log.warn("Not acsess : {}", id );
            throw new AppForbiddenException("Not access");
        }

        var article = checkOrGet(id);

        if (articleTypeRepository.findByKey(dto.getNameUz()) != null) {
            log.warn("Region alredy exists : {}", dto );
            throw new RegionAlreadyExistsException("Region alredy exists");
        }

        article.setNameUz(dto.getNameUz());
        article.setNameEn(dto.getNameEn());
        article.setNameRu(dto.getNameRu());
        article.setKey(dto.getKey());
        article.setProfileId(dto.getProfileId());

        articleTypeRepository.save(article);

        return "Success";
    }

    public List<ArticleTypeDTO> getAllByLang(LangEnum lang) {
        return articleTypeRepository.findAll()
                .stream()
                .map(entity-> toDTO(entity, lang))
                .toList();
    }

    public String delete(Integer id) {
        articleTypeRepository.delete(checkOrGet(id));
        return "Success";
    }


    /**
     * OTHER METHODS*/


    private ArticleTypeDTO toDTO(ArticleTypeEntity entity) {
        ArticleTypeDTO dto = new ArticleTypeDTO();
        dto.setId(entity.getId());
        dto.setNameUz(entity.getNameUz());
        dto.setNameEn(entity.getNameEn());
        dto.setNameRu(entity.getNameRu());
        dto.setKey(entity.getKey());
        dto.setProfileId(entity.getProfileId());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    private ArticleTypeDTO toDTO(ArticleTypeEntity entity, LangEnum lang) {
        ArticleTypeDTO dto = new ArticleTypeDTO();
        dto.setId(entity.getId());
        dto.setKey(entity.getKey());
        switch (lang) {
            case uz-> dto.setName(entity.getNameUz());
            case ru-> dto.setName(entity.getNameRu());
            case en-> dto.setName(entity.getNameEn());
        }
        return dto;
    }


    public ArticleTypeEntity checkOrGet(Integer id){
        return articleTypeRepository.findById(id)
                .orElseThrow(()->new AppBadRequestException("Id Not Found"));
    }
}
