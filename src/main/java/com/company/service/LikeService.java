package com.company.service;

import com.company.dto.request.LikeDetailDTO;
import com.company.dto.request.LikeRequestDTO;
import com.company.dto.responce.LikeCountDTO;
import com.company.dto.responce.LikeResponseDTO;
import com.company.entity.LikeEntity;
import com.company.enums.ProfileRole;
import com.company.exp.AppForbiddenException;
import com.company.exp.ItemNotFoundException;
import com.company.mapper.ArticleLikeDislikeMapper;
import com.company.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
@Slf4j
@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;

    public LikeResponseDTO create(LikeRequestDTO dto, Integer pId) {
        Optional<LikeEntity> oldLikeOptional = likeRepository.findByArticleIdAndProfileId(dto.getArticleId(), pId);

        if (oldLikeOptional.isPresent()) {
            var like=oldLikeOptional.get();
            like.setStatus(dto.getStatus());
            likeRepository.save(like);
            return toDTO(like);
        }

        LikeEntity entity = new LikeEntity();
        entity.setStatus(dto.getStatus());
        entity.setArticleId(dto.getArticleId());
        entity.setProfileId(pId);
        likeRepository.save(entity);

        return toDTO(entity);
    }

    public boolean update(Integer likeId, LikeDetailDTO dto, Integer pId) {

        if (!checkOrGet(likeId).getProfileId().equals(pId)) {
            throw new AppForbiddenException("Not Access");
        }
        return 0 < likeRepository.updateDetail(dto.getStatus(), LocalDateTime.now(), likeId);
    }

    public boolean delete(Integer likeId, Integer pId, ProfileRole role) {
        LikeEntity entity = checkOrGet(likeId);
        if (entity.getProfileId().equals(pId) || role.equals(ProfileRole.ADMIN)) {
            likeRepository.delete(entity);
            return true;
        }
        throw new AppForbiddenException("Not Access");
    }

    public PageImpl<LikeResponseDTO> listByArticleId(Integer articleId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size,Sort.Direction.DESC, "createdDate");

        var pagination = likeRepository.findAllByArticleId(articleId, pageable);

        var list=pagination
                .stream()
                .map(this::toDTO)
                .toList();

        return new PageImpl<>(list, pageable, pagination.getTotalElements());
    }

    public PageImpl<LikeResponseDTO> listByProfileId(Integer profileId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdDate");

        var pagination = likeRepository.findAllByProfileId(profileId, pageable);

        var list=pagination
                .stream()
                .map(this::toDTO)
                .toList();

        return new PageImpl<>(list, pageable, pagination.getTotalElements());
    }

    public PageImpl<LikeResponseDTO> list(int page, int size) {
        Pageable pageable = PageRequest.of(page, size,Sort.Direction.DESC, "createdDate");

        var pagination = likeRepository.findAll(pageable);

        var list = pagination
                .stream()
                .map(this::toDTO)
                .toList();

        return new PageImpl<>(list, pageable, pagination.getTotalElements());
    }

    public LikeResponseDTO getByArticleId(Integer articleId, Integer pId) {
        var entity= likeRepository.findByArticleIdAndProfileId(articleId, pId)
                .orElseThrow(()->new ItemNotFoundException("Item not found"));
        return toDTO(entity);
    }

    public LikeEntity checkOrGet(Integer likeId) {
        return likeRepository.findById(likeId).orElseThrow(() -> {
            throw new ItemNotFoundException("Like Not found");
        });
    }



    public LikeCountDTO getLIkeAndDislikeCount(Integer articleId) {
        ArticleLikeDislikeMapper mapper = likeRepository.countArticleLikeAndDislike(articleId);

        return new LikeCountDTO(
                mapper.getLike_count(),
                mapper.getLike_count());
    }


    public LikeResponseDTO toDTO(LikeEntity entity) {
        LikeResponseDTO dto = new LikeResponseDTO();
        dto.setId(entity.getId());
        dto.setStatus(entity.getStatus());
        dto.setProfileId(entity.getProfileId());
        dto.setArticleId(entity.getArticleId());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }
}
