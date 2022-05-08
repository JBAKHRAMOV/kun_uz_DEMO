package com.company;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Lesson88KunUzApplication {

    public static void main(String[] args) {
        SpringApplication.run(Lesson88KunUzApplication.class, args);
    }

    // CRUD - validation
    // List with pagination
    // Category - CRUD,List, multiLang - validation
    // Region - CRUD,List, multiLang  - validation
    // .http yoki PostMan
    //.
    // ArticleType - with Enum multiLang  - validation
    // ArticleType api - getList for admin

    // JWT + ArticleTyp,Category,Profile

    // 1.upload - separate
    // 2.Attach RUD with pagination
    // 3. Attach delete (from entity+system)
    // 4.Email History - RD + list with pagination
    // 5. 5.8-finish


    // Profile Image (ProfileImage)
    // login da profile image ham bo'lsin
    // Profile update ProfileImage (alohida API), DeleteProfileImge

    // in Authorization set profile image with profileDTO

    // Comment Entity ()


    // 1. LikeEntity
    // 2. Article (region,type,attach,viewCount,sharedCount)
    // getProfileArticle + pagination,

    // list
    // getArticleListByType last 5

    // USE simple version for the following API:
    // getArticleListBy Region
    // getArticleListBy Category
    // getArticleListBy Type
    // lastAdded4 articles
    // lastAdded 4 articles by region
    // lastAdded 4 articles by category


    // Publish (SuperModerator,PUBLISHER)
    // Block (SuperModerator,PUBLISHER)

    // public/{id} - full version (viewcount,sharedCount,linke,dislike,tag(lang),region(lang),category(lang),type (lang),)
    // admin/{id} - full version (may not be published)

    // increase view count
    // shared count increase (response generate sharedLink)



    // get user likeDTO by articleId (with jwt)
    // shu user shu article ga like/dislike bosganmi yo'qmi bilish uchun

    // Tag #yangilik, #guliston, #transformator, #gai, #manitor

    // kanikul zadaniya
    // 1. write project from scratch (0 dan) (vijdon)
    // 2. test all api
    // 3. online 5... full finish
    // 4. interviewQuestion
}
