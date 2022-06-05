package com.company.service;

import com.company.dto.request.ArticleTypeRequestDTO;
import com.company.dto.responce.ArticleTypeResponseDTO;
import com.company.entity.ArticleTypeEntity;
import com.company.enums.LangEnum;
import com.company.exp.AppBadRequestException;
import com.company.exp.RegionAlreadyExistsException;
import com.company.repository.ArticleTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleTypeService {
    private final ArticleTypeRepository articleTypeRepository;

    public ArticleTypeResponseDTO create(ArticleTypeRequestDTO dto, Integer pId) {

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

        return toDTO(entity);
    }

    public PageImpl<ArticleTypeResponseDTO> getList(int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        var pagination = articleTypeRepository.findAll(pageable);

        var list = pagination.stream().map(this::toDTO).toList();

        return new PageImpl<>(list, pageable, pagination.getTotalElements());
    }

    public ArticleTypeResponseDTO getById(Integer id) {
        return toDTO(checkOrGet(id));
    }

    public ArticleTypeResponseDTO getById(Integer id, LangEnum lang) {
        return toDTO(checkOrGet(id), lang);
    }

    public Boolean update(Integer id, ArticleTypeRequestDTO dto, Integer pId) {

        checkOrGet(id);

        if (articleTypeRepository.findByKey(dto.getNameUz()) != null) {
            log.warn("Region alredy exists : {}", dto );
            throw new RegionAlreadyExistsException("Region alredy exists");
        }

       return 0 <  articleTypeRepository.updateDetail(dto.getNameUz(), dto.getNameRu(), dto.getNameEn(), dto.getKey(), pId, LocalDateTime.now(), id);
    }

    public List<ArticleTypeResponseDTO> getAllByLang(LangEnum lang) {
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


    private ArticleTypeResponseDTO toDTO(ArticleTypeEntity entity) {
        ArticleTypeResponseDTO dto = new ArticleTypeResponseDTO();
        dto.setId(entity.getId());
        dto.setNameUz(entity.getNameUz());
        dto.setNameEn(entity.getNameEn());
        dto.setNameRu(entity.getNameRu());
        dto.setKey(entity.getKey());
        dto.setProfileId(entity.getProfileId());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    private ArticleTypeResponseDTO toDTO(ArticleTypeEntity entity, LangEnum lang) {
        ArticleTypeResponseDTO dto = new ArticleTypeResponseDTO();
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
