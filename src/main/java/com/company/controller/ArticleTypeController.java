package com.company.controller;

import com.company.dto.request.ArticleTypeRequestDTO;
import com.company.enums.LangEnum;
import com.company.enums.ProfileRole;
import com.company.service.ArticleTypeService;
import com.company.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/article_type")
public class ArticleTypeController {
    @Autowired
    private ArticleTypeService articleTypeService;

    @PostMapping("/adm")
    public ResponseEntity<?> create(@RequestBody @Valid ArticleTypeRequestDTO dto, HttpServletRequest request) {
        log.info("create : {}", dto );
        Integer profileId=JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(articleTypeService.create(dto,profileId ));
    }

    @GetMapping("/adm/pagination")
    public ResponseEntity<?> findAll(@RequestParam(value = "page", defaultValue = "0") int page,
                                     @RequestParam(value = "size", defaultValue = "3") int size) {
        log.info("find all : {}", "page:  "+page+" size: "+size );
        return ResponseEntity.ok(articleTypeService.getList(page, size));
    }

    @GetMapping("/adm/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
        log.info("get by id : {}", "id:  "+id );
        return ResponseEntity.ok(articleTypeService.getById(id));
    }

    @GetMapping("/public/list/{lang}")
    public ResponseEntity<?> getList(@PathVariable("lang") LangEnum lang) {
        log.info("get list : {}", lang );
        return ResponseEntity.ok(articleTypeService.getAllByLang(lang));
    }

    @PutMapping("/adm/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody @Valid ArticleTypeRequestDTO dto, HttpServletRequest request) {
        log.info("update : {}", "id:  "+id+" "+dto );
        Integer profileId=JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(articleTypeService.update(id, dto, profileId));
    }

    @DeleteMapping("/adm/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        log.info("delete : {}", id );
        return ResponseEntity.ok(articleTypeService.delete(id));
    }
}
