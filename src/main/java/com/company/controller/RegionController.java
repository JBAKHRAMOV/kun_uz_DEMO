package com.company.controller;


import com.company.dto.RegionDTO;
import com.company.enums.LangEnum;
import com.company.enums.ProfileRole;
import com.company.exp.TokenNotValidException;
import com.company.service.CategoryService;
import com.company.service.RegionService;
import com.company.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/region")
public class RegionController {
    @Autowired
    private RegionService regionService;

    @PostMapping("/adm")
    public ResponseEntity<?> create(@RequestBody @Valid RegionDTO dto,
                                    HttpServletRequest request) {
        log.info("create: {}", dto );
        Integer pid = JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(regionService.create(dto, pid));
    }

    @GetMapping("/adm")
    public ResponseEntity<?> list(HttpServletRequest request) {
        log.info("list: {}", "get all" );
        Integer pid = JwtUtil.getIdFromHeader(request);
        return ResponseEntity.ok(regionService.list());
    }

    @GetMapping("/public/{lang}")
    public ResponseEntity<?> list(@PathVariable("lang") LangEnum lang) {
        log.info("list: {}", lang );
        return ResponseEntity.ok(regionService.list(lang));
    }

    @PutMapping("/adm/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Integer id,
                                    @RequestBody @Valid RegionDTO dto,
                                    HttpServletRequest request) {
        log.info("update: {}", "id: "+id+"  "+dto );
        Integer pid = JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(regionService.update(id, dto));
    }

    @DeleteMapping("/adm/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id, HttpServletRequest request) {
        log.info("delete: {}", id );
        Integer pid = JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(regionService.delete(id));
    }
}
