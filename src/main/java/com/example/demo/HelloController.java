package com.example.demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
public class HelloController {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/")
    public String hello() {
        try {
            // Читаємо файл response.json безпосередньо з файлової системи контейнера.
            // Шлях /app/src/main/resources/response.json відповідає тому,
            // куди ми змонтуємо вашу локальну папку src/main/resources.
            String jsonContent = Files.readString(
                    Paths.get("/app/src/main/resources/response.json"), // Змінено шлях
                    StandardCharsets.UTF_8
            );

            // Парсимо JSON-рядок
            JsonNode rootNode = objectMapper.readTree(jsonContent);

            // Отримуємо значення поля "payload"
            if (rootNode.has("payload") && rootNode.get("payload").isTextual()) {
                String payloadValue = rootNode.get("payload").asText();
                return "H---\n" + payloadValue;
            } else {
                return "Error: 'payload' field not found or not a string in response.json.";
            }

        } catch (IOException e) {
            e.printStackTrace();
            return "Error reading or parsing response.json: " + e.getMessage();
        }
    }
}