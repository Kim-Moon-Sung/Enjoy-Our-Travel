package com.hackathon.eot.dto.response;

import com.hackathon.eot.model.dto.ArticleDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ArticleResponse {

    private Long id;
    private UserResponse user;
    private String title;
    private String content;

    public static ArticleResponse fromArticleDto(ArticleDto article) {
        return new ArticleResponse(
                article.getId(),
                UserResponse.fromUser(article.getUser()),
                article.getTitle(),
                article.getContent()
        );
    }
}
