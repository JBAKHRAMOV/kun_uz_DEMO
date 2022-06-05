package com.company.repository;

import com.company.entity.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {
    Page<CommentEntity> findAllByArticleId(Integer articleId, Pageable pageable);

    Page<CommentEntity> findAllByProfileId(Integer profile, Pageable pageable);

    @Transactional
    @Modifying
    @Query("update CommentEntity set content =:content,  updatedDate=:updDate where id =:id")
    int updateDetail(@Param("content") String content, @Param("updDate") LocalDateTime updDate, @Param("id") Integer id);

}
