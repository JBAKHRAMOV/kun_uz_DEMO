package com.company.service;

import com.company.dto.request.CategoryRequestDTO;
import com.company.dto.responce.CategoryResponseDTO;
import com.company.entity.CategoryEntity;
import com.company.enums.LangEnum;
import com.company.exp.AppBadRequestException;
import com.company.exp.CategoryAlredyExistsException;
import com.company.repository.CategoryRepository;
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
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryResponseDTO create(CategoryRequestDTO dto, Integer pId) {

        if (categoryRepository.findByKey(dto.getKey()) != null) {
            log.warn("category alredy exists : {}", dto );
            throw new CategoryAlredyExistsException("Category Already Exists");
        }

        CategoryEntity entity = new CategoryEntity();
        entity.setNameUz(dto.getNameUz());
        entity.setNameEn(dto.getNameEn());
        entity.setNameRu(dto.getNameRu());
        entity.setKey(dto.getKey());
        entity.setProfileId(pId);

        categoryRepository.save(entity);

        return toDTO(entity);
    }

    public PageImpl<CategoryResponseDTO> getList(int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        var pagination = categoryRepository.findAll(pageable);

        var list = pagination
                .stream()
                .map(this::toDTO)
                .toList();

        return new PageImpl<>(list, pageable, pagination.getTotalElements());
    }

    public CategoryResponseDTO getById(Integer id) {
        return toDTO(checkOrGet(id));
    }

    public CategoryResponseDTO getById(Integer id, LangEnum lang) {
        return toDTO(checkOrGet(id), lang);
    }

    public Boolean update(Integer id, CategoryRequestDTO dto, Integer pId) {

        if (categoryRepository.findByKey(dto.getKey()) != null) {
            log.warn("category alredy exists : {}", dto );
            throw new CategoryAlredyExistsException("Category alredy exists");
        }

        return 0< categoryRepository.updateDetail(dto.getNameUz(), dto.getNameRu(), dto.getNameEn(), dto.getKey(), pId, LocalDateTime.now(), id);
    }

    public String delete(Integer id) {
        categoryRepository.delete(checkOrGet(id));
        return "Success";
    }

    public List<CategoryResponseDTO> getListByLang(LangEnum lang) {
        return categoryRepository.findAll()
                .stream()
                .map(entity->toDTO(entity, lang))
                .toList();
    }



    /**
     * OTHER METHODS*/

    private CategoryResponseDTO toDTO(CategoryEntity entity) {
        CategoryResponseDTO dto = new CategoryResponseDTO();
        dto.setId(entity.getId());
        dto.setNameUz(entity.getNameUz());
        dto.setNameEn(entity.getNameEn());
        dto.setNameRu(entity.getNameRu());
        dto.setKey(entity.getKey());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setProfileId(entity.getProfileId());
        return dto;
    }

    private CategoryResponseDTO toDTO(CategoryEntity entity, LangEnum lang) {
        CategoryResponseDTO dto = new CategoryResponseDTO();
        dto.setId(entity.getId());
        dto.setKey(entity.getKey());
        switch (lang) {
            case uz -> dto.setName(entity.getNameUz());
            case ru -> dto.setName(entity.getNameRu());
            case en -> dto.setName(entity.getNameEn());
        }
        return dto;
    }

    public CategoryEntity checkOrGet(Integer id){
         return categoryRepository.findById(id)
                 .orElseThrow(()->new AppBadRequestException("Id Not Found"));
    }
}
