package com.example.demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource; // Повертаємо цей імпорт
import org.springframework.util.StreamUtils; // Повертаємо цей імпорт
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream; // Повертаємо цей імпорт
import java.nio.charset.StandardCharsets;
// import java.nio.file.Files; // Ці імпорти більше не потрібні для продакшену
// import java.nio.file.Paths; // Ці імпорти більше не потрібні для продакшену

@RestController
public class HelloController {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/")
    public String hello() {
        try {
            // Читаємо файл response.json з classpath (зсередини JAR-файлу).
            // Це правильний спосіб для продакшн-розгортання, де файли ресурсів
            // упаковані разом з додатком.
            ClassPathResource resource = new ClassPathResource("response.json");

            // Перевіряємо, чи існує ресурс, перш ніж намагатися його прочитати
            if (!resource.exists()) {
                throw new IOException("Resource 'response.json' not found in classpath!");
            }

            // Отримуємо InputStream з ресурсу
            // Використовуємо try-with-resources для автоматичного закриття InputStream
            String jsonContent;
            try (InputStream inputStream = resource.getInputStream()) {
                // Читаємо вміст InputStream у String, використовуючи StreamUtils
                jsonContent = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            }

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
