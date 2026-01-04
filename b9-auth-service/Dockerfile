# 1. Используем официальный образ OpenJDK 17
FROM eclipse-temurin:17-jdk-jammy

# 2. Создаем рабочую директорию в контейнере
WORKDIR /app

# 3. Копируем собранный JAR-файл в контейнер.
COPY target/*.jar app.jar

# 4. Определяем команду запуска приложения
ENTRYPOINT ["java", "-jar", "app.jar"]