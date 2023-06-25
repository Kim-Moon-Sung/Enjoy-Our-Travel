package com.hackathon.eot.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
public class ArticleCreateRequest {
    private String title;
    private String content;
    private List<MultipartFile> files;
}
