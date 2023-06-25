package com.hackathon.eot.controller;

import com.hackathon.eot.dto.request.ArticleCreateRequest;
import com.hackathon.eot.dto.request.PostCommentRequest;
import com.hackathon.eot.dto.response.Response;
import com.hackathon.eot.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping
    public Response<Void> create(ArticleCreateRequest request, Authentication authentication) throws Exception {
        articleService.create(request.getTitle(), request.getContent(), authentication.getName(), request.getFiles());
        return Response.success();
    }

    @PostMapping("/{articleId}/comments")
    public Response<Void> comment(@PathVariable Long articleId, @RequestBody PostCommentRequest request, Authentication authentication) {
        articleService.comment(articleId, authentication.getName(), request.getContent());
        return Response.success();
    }
}
