package com.company.controller;

import com.company.dto.ProfileJwtDTO;
import com.company.service.LikeService;
import com.company.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/like")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @PostMapping("/profile")
    public ResponseEntity<?> create(@RequestBody @Valid LikeDTO dto,
                                    HttpServletRequest request) {
        log.info("create: {}", dto );
        Integer pId = JwtUtil.getIdFromHeader(request);
        return ResponseEntity.ok(likeService.create(dto, pId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer commentId,
                                    HttpServletRequest request) {
        log.info("delete: {}", commentId );
        ProfileJwtDTO jwtDTO = JwtUtil.getProfileFromHeader(request);
        return ResponseEntity.ok(likeService.delete(commentId, jwtDTO.getId(), jwtDTO.getRole()));
    }

    @GetMapping("/article/{id}")
    public ResponseEntity<?> findAllByArticleId(@PathVariable("id") Integer articleId,
                                                @RequestParam(value = "page", defaultValue = "0") int page,
                                                @RequestParam(value = "size", defaultValue = "3") int size) {
        log.info("find all by article id: {}", "page: "+page+" size: "+size );
        return ResponseEntity.ok(likeService.listByArticleId(articleId, page, size));
    }

    @GetMapping("/adm/profile/{id}")
    public ResponseEntity<?> findAllByProfileId(@PathVariable("id") Integer profileId,
                                                @RequestParam(value = "page", defaultValue = "0") int page,
                                                @RequestParam(value = "size", defaultValue = "3") int size) {
        log.info("find all by profile id: {}", "page: "+page+" size: "+size );
        return ResponseEntity.ok(likeService.listByProfileId(profileId, page, size));
    }

    @GetMapping("/adm")
    public ResponseEntity<?> findAll(@RequestParam(value = "page", defaultValue = "0") int page,
                                     @RequestParam(value = "size", defaultValue = "3") int size) {
        log.info("find all: {}", "page: "+page+" size: "+size );
        return ResponseEntity.ok(likeService.list(page, size));
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<?> findByProfile(@PathVariable("id") Integer articleId,
                                           HttpServletRequest request) {
        log.info("find all by profile: {}", articleId );
        Integer pId = JwtUtil.getIdFromHeader(request);
        return ResponseEntity.ok(likeService.getByArticleId(articleId, pId));
    }


}
