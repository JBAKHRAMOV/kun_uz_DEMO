package com.company.controller;

import com.company.enums.ArticleStatus;
import com.company.enums.LangEnum;
import com.company.enums.ProfileRole;
import com.company.service.ArticleService;
import com.company.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    /**
     * Public
     */

    @GetMapping("/public/list")
    public ResponseEntity<?> listByLang(@RequestParam(value = "page", defaultValue = "0") int page,
                                        @RequestParam(value = "size", defaultValue = "5") int size) {
        log.info("list by Lang: {}", "page: "+page +" size: "+ size);
        return ResponseEntity.ok(articleService.list(page, size));
    }

    @GetMapping("/public/type/{id}")
    public ResponseEntity<?> getByType(@PathVariable("id") Integer typeId) {
        log.info("get by Type: {}", "id: "+typeId);
        return ResponseEntity.ok(articleService.getTop5ByTypeId(typeId));
    }


    @GetMapping("/public/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Integer articleId,
                                     @RequestHeader(name = "Accepted-Language", defaultValue = "uz") LangEnum lang) {
        log.info("get by id: {}", "id: "+articleId +" lang: "+ lang);
        return ResponseEntity.ok(articleService.getByIdPublished(articleId, lang));
    }

    @GetMapping("/public/region/{regionId}")
    public ResponseEntity<?> publishedListByRegionId(@RequestParam(value = "page", defaultValue = "0") int page,
                                                     @RequestParam(value = "size", defaultValue = "5") int size,
                                                     @PathVariable("regionId") Integer rId) {
        log.info("published list by region: {}", "page: "+page +" size: "+ size);
        return ResponseEntity.ok(articleService.publishedListByRegion(page, size, rId));
    }

    @GetMapping("/public/category/{categoryId}")
    public ResponseEntity<?> publishedListByCategoryId(@RequestParam(value = "page", defaultValue = "0") int page,
                                                       @RequestParam(value = "size", defaultValue = "5") int size,
                                                       @PathVariable("categoryId") Integer cId) {
        log.info("published list by category id: {}", "page: "+page +" size: "+ size);
        return ResponseEntity.ok(articleService.publishedListByCategoryId(page, size, cId));
    }

    @GetMapping("/public/type/{typeId}")
    public ResponseEntity<?> publishedListByTypeId(@RequestParam(value = "page", defaultValue = "0") int page,
                                                   @RequestParam(value = "size", defaultValue = "5") int size,
                                                   @PathVariable("typeId") Integer tId) {
        log.info("published list by type id: {}", "page: "+page +" size: "+ size);
        return ResponseEntity.ok(articleService.publishedListByTypeId(page, size, tId));
    }

    @GetMapping("/public/last")
    public ResponseEntity<?> getLastAdded4Article() {
        log.info("published list by category id: {}");
        return ResponseEntity.ok(articleService.last4());
    }

    @GetMapping("/public/region/{regionId}/4")
    public ResponseEntity<?> top4ByRegionId(@PathVariable("regionId") Integer rId) {
        log.info("top 4 by region id : {}", "regionId: "+rId );
        return ResponseEntity.ok(articleService.top4ByRegionId(rId));
    }

    @GetMapping("/public/category/{categoryId}/4")
    public ResponseEntity<?> top4ByCategoryId(@PathVariable("categoryId") Integer cId) {
        log.info("top 4 by catgory id : {}", "category: "+cId );
        return ResponseEntity.ok(articleService.top4ByCategoryId(cId));
    }

    @GetMapping("/public/share/{id}")
    public ResponseEntity<?> generateShareLink(@RequestHeader(name = "Accepted-Language", defaultValue = "uz") LangEnum lang,
                                               @PathVariable("id") Integer id) {
        log.info("generete share link : {}", "id: "+id+ "lang: "+lang );
        return ResponseEntity.ok(articleService.getShared(lang, id));
    }

    @GetMapping("/public/view/{id}")
    public ResponseEntity<?> increaseViewCount(@PathVariable("id") Integer id) {
        log.info("increase view count : {}", "id: "+id);
        articleService.updateViewCount(id);
        return ResponseEntity.ok().build();
    }

    /**
     * ADMIN
     */

    @PostMapping("/adm")
    public ResponseEntity<?> create(@RequestBody @Valid ArticleDTO dto, HttpServletRequest request) {
        log.info("create : {}", dto );
        Integer pId = JwtUtil.getIdFromHeader(request, ProfileRole.MODERATOR);
        return ResponseEntity.ok(articleService.create(dto, pId));
    }


    @PutMapping("/adm/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Integer id,
                                    @RequestBody ArticleDTO dto,
                                    HttpServletRequest request) {
        log.info("update : {}", "id: "+id+" " + dto );
        Integer pId = JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(articleService.update(id, dto, pId));
    }

    @PutMapping("/adm/{aId}/status")
    public ResponseEntity<?> changeStatus(@PathVariable("aId") Integer aId,
                                          @RequestParam  ArticleStatus status,
                                          HttpServletRequest request) {
        log.info("change status : {}", "id: "+aId+" status: " + status );
        Integer pId = JwtUtil.getIdFromHeader(request, ProfileRole.PUBLISHER, ProfileRole.MODERATOR);
        return ResponseEntity.ok(articleService.changeStatus(aId, status));
    }

    @GetMapping("/adm/{id}")
    public ResponseEntity<?> getByIdAsAdmin(@PathVariable("id") Integer articleId,
                                            @RequestHeader(name = "Accepted-Language", defaultValue = "uz") LangEnum lang) {
        log.info("get by id as admin : {}", "id: "+articleId+" lang: " + lang );
        return ResponseEntity.ok(articleService.getByIdAdAdmin(articleId, lang));
    }

    @DeleteMapping("/adm/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id, HttpServletRequest request) {
        log.info("delete : {}", "id: "+id);
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(articleService.delete(id));
    }
}
