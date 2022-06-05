package com.company.repository;

import com.company.entity.ProfileEntity;
import com.company.enums.ProfileStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<ProfileEntity, Integer> {

    Optional<ProfileEntity> findByEmail(String email);

    Page<ProfileEntity> findByVisible(Boolean visible, Pageable pageable);

    @Transactional
    @Modifying
    @Query("update ProfileEntity set visible = :visible where id = :id")
    int updateVisible(@Param("visible") Boolean visible, @Param("id") Integer id);

//entity.setName(dto.getName());
//        entity.setSurname(dto.getSurname());
//        entity.setEmail(dto.getEmail());
//        entity.setPassword(dto.getPassword());
//        entity.setUpdatedDate(LocalDateTime.now());
    @Transactional
    @Modifying
    @Query("update ProfileEntity set name = :name, surname=:surname, email=:email, password=:password, updatedDate=:upd where id = :id")
    int updateDetail(@Param("name") String name,@Param("surname") String surname,  @Param("email") String email,
                     @Param("password") String password, @Param("upd") LocalDateTime upd,@Param("id") Integer id);

    Optional<ProfileEntity> findByEmailAndPassword(String email, String password);

    @Transactional
    @Modifying
    @Query("update ProfileEntity set status = :status where id = :id")
    int updateStatus(@Param("status") ProfileStatus status, @Param("id") Integer id);

    @Transactional
    @Modifying
    @Query("update ProfileEntity set attach.id = :attachId where id = :id")
    int updateAttach(@Param("attachId") String attachId, @Param("id") Integer id);
}
