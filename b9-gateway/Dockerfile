FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# ПРЕДУПРЕЖДЕНИЕ: Замените 'api-gateway-0.0.1-SNAPSHOT.jar' на ваше фактическое имя JAR-файла
COPY target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]