package com.company.repository;

import com.company.entity.ArticleTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

public interface ArticleTypeRepository extends JpaRepository<ArticleTypeEntity, Integer> {
    ArticleTypeEntity findByKey(String key);@Transactional


    @Modifying
    @Query("update ArticleTypeEntity set nameUz =:nUz, nameRu=:nRu, nameEn=:nEn, key=:key, profileId=:pId, updatedDate=:updDate where id =:id")
    int updateDetail(@Param("nUz") String nUz, @Param("nRu") String nRu, @Param("nEn") String nEn, @Param("key") String key,
                     @Param("pId") Integer pId, @Param("updDate") LocalDateTime updDate, @Param("id") Integer id);
}
