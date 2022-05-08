package com.company.controller;

import com.company.dto.AttachDTO;
import com.company.dto.ProfileDTO;
import com.company.service.ProfileService;
import com.company.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @PostMapping("/adm")
    public ResponseEntity<?> createProfile(@RequestBody @Valid ProfileDTO dto) {
        log.info("create profile: {}",dto );
        return ResponseEntity.ok(profileService.create(dto));
    }

    @GetMapping("/adm")
    public ResponseEntity<?> getProfileList(@RequestParam(value = "page", defaultValue = "0") int page,
                                            @RequestParam(value = "size", defaultValue = "5") int size) {
        log.info("get profile list: {}", "page: "+page+" size: "+size );
        return ResponseEntity.ok(profileService.paginationList(page, size));
    }

    @GetMapping("/adm/{id}")
    public ResponseEntity<?> getProfileById(@PathVariable("id") Integer id) {
        log.info("get profile by id: {}", id );
        return ResponseEntity.ok(profileService.getById(id));
    }

    @PutMapping("/adm/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable("id") Integer id,
                                           @RequestBody @Valid ProfileDTO dto) {
        log.info("update student: {}", "id: "+id+"  "+dto );
        return ResponseEntity.ok(profileService.update(id, dto));
    }

    @DeleteMapping("/adm/delete/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable("id") Integer id) {
        log.info("delete student: {}", id );
        return ResponseEntity.ok(profileService.delete(id));
    }


    @PostMapping("/image")
    public ResponseEntity<?> updateImage(@RequestBody AttachDTO image,
                                         HttpServletRequest request) {
        log.info("update image: {}", image );
        Integer pId = JwtUtil.getIdFromHeader(request);
        try {
            return ResponseEntity.ok(profileService.updateImage(image.getId(), pId));
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Attach not found");
        }


    }


}
