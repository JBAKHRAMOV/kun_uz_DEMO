package com.company.service;

import com.company.dto.AttachDTO;
import com.company.dto.AuthDTO;
import com.company.dto.ProfileDTO;
import com.company.dto.RegistrationDTO;
import com.company.entity.AttachEntity;
import com.company.entity.ProfileEntity;
import com.company.enums.ProfileRole;
import com.company.enums.ProfileStatus;
import com.company.exp.AppBadRequestException;
import com.company.exp.AppForbiddenException;
import com.company.exp.EmailAlreadyExistsException;
import com.company.exp.PasswordOrEmailWrongException;
import com.company.repository.ProfileRepository;
import com.company.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final ProfileRepository profileRepository;
    private final EmailService emailService;
    private final AttachService attachService;

    public ProfileDTO login(AuthDTO dto) {
        String pswd = DigestUtils.md5Hex(dto.getPassword());

        var entity = profileRepository.findByEmailAndPassword(dto.getEmail(), pswd)
                .orElseThrow(()->{
                    log.info("password or password wrong : {}", dto );
                    throw new PasswordOrEmailWrongException("Password or email wrong!");
                });

        if (!entity.getStatus().equals(ProfileStatus.ACTIVE)) {
            log.warn("no access : {}", dto );
            throw new AppForbiddenException("No Access bratishka.");
        }

        ProfileDTO profile = new ProfileDTO();

        profile.setEmail(entity.getEmail());
        profile.setName(entity.getName());
        profile.setSurname(entity.getSurname());
        profile.setRole(entity.getRole());
        profile.setJwt(
                JwtUtil.encode(
                        entity.getId(),
                        entity.getRole()));

        // image
        AttachEntity image = entity.getAttach();
        if (image != null)
            profile.setImage(
                    new AttachDTO(
                            attachService.toOpenURL(
                                    entity.getAttach().getId())));

        return profile;
    }

    public void registration(RegistrationDTO dto) {

        profileRepository.findByEmail(dto.getEmail())
                .orElseThrow(()->{
                    log.warn("Email already axists : {}", dto );
                    throw new EmailAlreadyExistsException("Email Already Exits");
                });

        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setPassword(DigestUtils.md5Hex(dto.getPassword()));

        entity.setRole(ProfileRole.USER);
        entity.setStatus(ProfileStatus.NOT_ACTIVE);
        profileRepository.save(entity);

        Thread thread = new Thread() {
            @Override
            public void run() {
                sendVerificationEmail(entity);
            }
        };
        thread.start();
    }


    public void verification(String jwt) {
        Integer userId = null;
        try {
            userId = JwtUtil.decodeAndGetId(jwt);
        } catch (JwtException e) {
            log.warn("verification not completed: {}", jwt );
            throw new AppBadRequestException("Verification not completed");
        }
        profileRepository.updateStatus(ProfileStatus.ACTIVE, userId);
    }

    private void sendVerificationEmail(ProfileEntity entity) {
        StringBuilder builder = new StringBuilder();
        String jwt = JwtUtil.encode(entity.getId());
        builder.append("To verify your registration click to next link.");
        builder.append("http://localhost:8080/auth/verification/").append(jwt);
        emailService.send(entity.getEmail(), "Activate Your Registration", builder.toString());

    }
}
