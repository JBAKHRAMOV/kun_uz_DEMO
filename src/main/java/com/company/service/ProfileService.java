package com.company.service;

import com.company.dto.request.ProfileRequestDTO;
import com.company.dto.responce.ProfileResponseDTO;
import com.company.entity.ProfileEntity;
import com.company.enums.ProfileStatus;
import com.company.exp.EmailAlreadyExistsException;
import com.company.exp.ItemNotFoundException;
import com.company.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final AttachService attachService;

    public ProfileResponseDTO create(ProfileRequestDTO dto) {

        Optional<ProfileEntity> optional = profileRepository.findByEmail(dto.getEmail());
        if (optional.isPresent()) {
            log.warn("email alredy exists : {}", dto );
            throw new EmailAlreadyExistsException("Email Already Exits");
        }

        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setPassword(dto.getPassword());
        entity.setRole(dto.getRole());
        entity.setStatus(ProfileStatus.ACTIVE);

        profileRepository.save(entity);

        return toDTO(entity);
    }

    public PageImpl<ProfileResponseDTO> paginationList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdDate");

        var pagination=profileRepository.findByVisible(true, pageable);

        var list =pagination
                .stream()
                .map(this::toDTO)
                .toList();

        return new PageImpl<>(list, pageable, pagination.getTotalElements());
    }

    public ProfileResponseDTO getById(Integer id) {
        return toDTO(getVisible(id));
    }

    public  Boolean update(Integer id, ProfileRequestDTO dto) {

        if (profileRepository.findByEmail(dto.getEmail()).isPresent()) {
            log.warn("email alredy used : {}", dto );
            throw new EmailAlreadyExistsException("This Email already used!");
        }

        checkOrGet(id);

       return 0 <  profileRepository.updateDetail(dto.getName(), dto.getSurname(), dto.getEmail(), dto.getPassword(), LocalDateTime.now(), id);
    }

    public Boolean delete(Integer id) {
        return 0 < profileRepository.updateVisible(false, getVisible(id).getId());
    }

    public boolean updateImage(String attachId, Integer pId) {
        ProfileEntity profileEntity = checkOrGet(pId);

        if (profileEntity.getAttach() != null) {
            attachService.delete(profileEntity.getAttach().getId());
        }
        profileRepository.updateAttach(attachId, pId);

        return true;
    }


    /**
     * OTHER METHODS*/

    private ProfileResponseDTO toDTO(ProfileEntity entity) {
        ProfileResponseDTO dto = new ProfileResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setEmail(entity.getEmail());
        dto.setPassword(entity.getPassword());
        dto.setUpdatedDate(entity.getUpdatedDate());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    public ProfileEntity getVisible(Integer id) {

        ProfileEntity entity = profileRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Not Found!"));
        if (!entity.getVisible()) {
            log.warn(" not found : {},",id );
            throw new ItemNotFoundException("Not Found!");
        }
        return entity;
    }

    public ProfileEntity checkOrGet(Integer id) {
        return profileRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Not Found!"));
    }
}
