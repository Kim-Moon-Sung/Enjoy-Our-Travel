package com.hackathon.eot.controller;

import com.hackathon.eot.dto.request.ArticleCreateRequest;
import com.hackathon.eot.dto.request.PostCommentRequest;
import com.hackathon.eot.dto.response.ArticleResponse;
import com.hackathon.eot.dto.response.CommentResponse;
import com.hackathon.eot.dto.response.Response;
import com.hackathon.eot.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @GetMapping
    public Response<Page<ArticleResponse>> articles(Pageable pageable) {
        return Response.success(articleService.articles(pageable).map(ArticleResponse::fromArticleDto));
    }

    @GetMapping("/{articleId}")
    public Response<ArticleResponse> article(@PathVariable Long articleId) {
        return Response.success(ArticleResponse.fromArticleDto(articleService.article(articleId)));
    }

    @GetMapping("/{articleId}/comments")
    public Response<Page<CommentResponse>> comments(@PathVariable Long articleId, Pageable pageable) {
        return Response.success(articleService.comments(articleId, pageable).map(CommentResponse::fromCommentDto));
    }

    @PostMapping("/{articleId}/comments")
    public Response<Void> comment(@PathVariable Long articleId, @RequestBody PostCommentRequest request, Authentication authentication) {
        articleService.comment(articleId, authentication.getName(), request.getContent());
        return Response.success();
    }
}
