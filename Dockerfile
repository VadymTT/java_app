# Використовуємо офіційний образ з Open
# FROM openjdk:21
# COPY . /app
# WORKDIR /app
# RUN javac src/test/java/AppHelloWorld.java -d .
# CMD ["java", "AppHelloWorld"]

# Етап збірки (використовуємо JDK для компіляції)
FROM openjdk:17-jdk-slim AS build

# Встановлюємо робочу директорію всередині контейнера
WORKDIR /app

# Копіюємо файли Maven-обгортки та pom.xml
# Це дозволяє Docker кешувати крок завантаження залежностей.
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Завантажуємо залежності Maven в офлайн-режимі.
# Це прискорює подальші збірки, оскільки залежності вже будуть завантажені.
RUN ./mvnw dependency:go-offline

# Копіюємо вихідний код програми
COPY src ./src

# Компілюємо та збираємо проект, пропускаючи тести.
# Якщо вам потрібні тести, видаліть -DskipTests.
RUN ./mvnw clean install -DskipTests

# Етап виконання
# Використовуємо openjdk:17-slim для запуску програми.
# Зверніть увагу: openjdk:17-jre-slim не існує.
# openjdk:17-slim містить JDK, але є найближчим "slim" варіантом.
FROM openjdk:17-slim

# Встановлюємо робочу директорію для запуску програми
WORKDIR /app

# Копіюємо зібраний JAR-файл з етапу збірки
# app.jar - це назва вашого виконуваного JAR-файлу,
# переконайтеся, що вона відповідає назві, яку генерує ваш Maven-проект.
COPY --from=build /app/target/*.jar app.jar

# Відкриваємо порт, на якому слухає ваш додаток.
# Переконайтеся, що 8080 відповідає порту, який використовує ваш додаток Spring Boot або інший Java-додаток.
EXPOSE 8080

# Визначаємо команду для запуску програми.
# app.jar має бути назвою вашого зібраного JAR-файлу.
ENTRYPOINT ["java", "-jar", "app.jar"]
