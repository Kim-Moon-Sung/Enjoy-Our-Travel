package com.hackathon.eot.api;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class ImageVariationService {

    @Value("${kakao.rest.api.key}")
    private String kakaoRestApiKey;

    private final String KAKAO_KARLO_REQUEST_URI = "https://api.kakaobrain.com/v1/inference/karlo/variations";

    private final RestTemplate restTemplate;

    public String variations(String imageBase64) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "KakaoAK " + kakaoRestApiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestBody = "{\"prompt\": {\"image\": \"" + imageBase64 + "\", \"batch_size\": 1}}";
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(KAKAO_KARLO_REQUEST_URI, requestEntity, String.class);
        return response.getBody();
    }

    // 이미지를 Base64 인코딩하기
    public String imageToBase64(BufferedImage image) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.getEncoder().encodeToString(imageBytes);
    }

    // Base64 디코딩 및 변환
    public void base64ToImage(String base64String) throws Exception {
        byte[] data = DatatypeConverter.parseBase64Binary(base64String);
        String absolutePath = new File("").getAbsolutePath() + File.separator + File.separator;
        String path = "images" + File.separator + "result";
        File file = new File(path);

        if(!file.exists()) {
            boolean wasSuccessful = file.mkdirs();
        }

        String fileName = "kakaoResult.png";
        file = new File(absolutePath + path + File.separator + fileName);

        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
        outputStream.write(data);

        file.setReadable(true);
        file.setWritable(true);
    }

    public void createImage(String filePath) throws Exception {
        File imageFile = new File(filePath);
        BufferedImage image = ImageIO.read(imageFile);

        // 이미지 Base64 인코딩
        String imageBase64 = imageToBase64(image);

        // 이미지 변환하기 REST API 호출
        String response = variations(imageBase64);

        // 응답의 첫 번째 이미지 생성 결과 출력하기
        String firstImageBase64 = extractFirstImageBase64(response);
        base64ToImage(firstImageBase64);
    }

    private static String extractFirstImageBase64(String response) {
        // JSON 파싱 및 첫 번째 이미지의 Base64 데이터 추출
        // 파싱 라이브러리를 이용하여 JSON 을 파싱하는 것이 좋음
        String base64Start = "\"image\":\"";
        int startIdx = response.indexOf(base64Start) + base64Start.length();
        int endIdx = response.indexOf("\"", startIdx);
        return response.substring(startIdx, endIdx);
    }
}
