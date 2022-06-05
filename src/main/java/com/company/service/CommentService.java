package com.company.service;

import com.company.dto.request.CommentDetailDTO;
import com.company.dto.request.CommentRequestDTO;
import com.company.dto.responce.CommentResponseDTO;
import com.company.entity.CommentEntity;
import com.company.enums.ProfileRole;
import com.company.exp.AppForbiddenException;
import com.company.exp.ItemNotFoundException;
import com.company.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ArticleService articleService;

    public CommentResponseDTO create(CommentRequestDTO dto, Integer pId) {

        articleService.checkOrGet(dto.getArticleId());

        CommentEntity entity = new CommentEntity();
        entity.setContent(dto.getContent());
        entity.setArticleId(dto.getArticleId());
        entity.setProfileId(pId);

        commentRepository.save(entity);

        return toDTO(entity);
    }

    public boolean update(Integer commentId, CommentDetailDTO dto, Integer pId) {

        if (!checkOrGet(commentId).getProfileId().equals(pId)) {
            log.warn("not access : {}", dto );
            throw new AppForbiddenException("Not Access");
        }

        return 0 < commentRepository.updateDetail(dto.getContent(), LocalDateTime.now(), commentId);
    }

    public boolean delete(Integer commentId, Integer pId, ProfileRole role) {

        CommentEntity entity = checkOrGet(commentId);

        if (entity.getProfileId().equals(pId) || role.equals(ProfileRole.ADMIN)) {
            commentRepository.delete(entity);
            return true;
        }
        throw new AppForbiddenException("Not Access");
    }

    public PageImpl<CommentResponseDTO> listByArticleId(Integer articleId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdDate");

        var pagination = commentRepository.findAllByArticleId(articleId, pageable);

        var list = pagination
                .stream()
                .map(this::toDTO)
                .toList();

        return new PageImpl<>(list, pageable, pagination.getTotalElements());
    }

    public PageImpl<CommentResponseDTO> listByProfileId(Integer profileId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdDate");

        var pagination = commentRepository.findAllByProfileId(profileId, pageable);

        var list=pagination
                .stream()
                .map(this::toDTO)
                .toList();

        return new PageImpl<>(list, pageable, pagination.getTotalElements());
    }

    public PageImpl<CommentResponseDTO> list(int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdDate");

        var pagination = commentRepository.findAll(pageable);

        var list=pagination
                .stream()
                .map(this::toDTO)
                .toList();

        return new PageImpl<>(list, pageable, pagination.getTotalElements());
    }



    /**
     * OTHER METHODS*/


    public CommentEntity checkOrGet(Integer commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> {
            log.warn("id not found : {}", commentId );
            throw new ItemNotFoundException("Comment Not found");
        });
    }

    public CommentResponseDTO toDTO(CommentEntity entity) {
        CommentResponseDTO dto = new CommentResponseDTO();
        dto.setId(entity.getId());
        dto.setContent(entity.getContent());
        dto.setProfileId(entity.getProfileId());
        dto.setArticleId(entity.getArticleId());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setUpdatedDate(entity.getUpdatedDate());
        return dto;
    }

}
