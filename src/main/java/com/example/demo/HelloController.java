package com.example.demo;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

@RestController
public class HelloController {

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getJsonResponse() {
        try {
            // Читаємо файл response.json з ресурсів
            ClassPathResource resource = new ClassPathResource("response.json");
            InputStreamReader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
            String jsonContent = FileCopyUtils.copyToString(reader);

            // Повертаємо JSON з правильним Content-Type
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(jsonContent);

        } catch (IOException e) {
            // У випадку помилки, повертаємо внутрішню помилку сервера
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body("{\"error\": \"Не вдалося прочитати response.json: " + e.getMessage() + "\"}");
        }
    }
}