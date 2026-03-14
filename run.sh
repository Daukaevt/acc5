#!/bin/bash

# Останавливаем скрипт при любой ошибке (но для фоновых задач сделаем ручную проверку)
set +e 

echo "🚀 Запуск системы Аккредитация-2026 (Safe Turbo Mode)..."

# 1. Создаем волюм для кэша, если его еще нет
docker volume create maven-repo > /dev/null 2>&1

# Список сервисов
SERVICES=(
  "b9-eureka"
  "b9-auth-service"
  "b9-gateway"
  "b9-hello-world-service"
  "b9-exception-service"
  "b9-client-service"
)

# 2. Сборка JAR-файлов
echo "📦 Начинаем сборку сервисов..."
for service in "${SERVICES[@]}"; do
    if [ -d "$service" ]; then
        echo "🛠 Сборка $service запущена..."
        # Добавляем небольшую задержку (2 сек), чтобы Maven не дрался за один и тот же файл в кэше при старте
        sleep 2
        docker run --rm \
          -v "$(pwd)/$service":/usr/src/app \
          -v maven-repo:/root/.m2 \
          -w /usr/src/app \
          maven:3.8-openjdk-17 \
          mvn clean package -DskipTests -q & 
    fi
done

echo "⏳ Ждем завершения всех сборок. Это может занять пару минут..."
wait

# 3. КРИТИЧЕСКАЯ ПРОВЕРКА: Все ли JAR собрались?
echo "🔍 Проверяем артефакты..."
FAILED=0
for service in "${SERVICES[@]}"; do
    # Ищем jar в папке target
    JAR_FILE=$(find "$service/target" -name "*.jar" 2>/dev/null)
    if [ -z "$JAR_FILE" ]; then
        echo "❌ ОШИБКА: Сервис $service не собрался (JAR не найден)."
        FAILED=1
    fi
done

if [ $FAILED -eq 1 ]; then
    echo "🛑 Остановка: один или несколько сервисов не собрались. Проверь логи Maven выше."
    exit 1
fi

echo "✅ Все JAR-файлы успешно собраны!"

# 4. Поднимаем инфраструктуру
echo "🏗 Шаг 1: Поднимаем базы и Eureka..."
docker compose up -d --build eureka-server vault auth-db photo-db exception-db

# 5. Проверка Vault
echo "🔐 Ждем готовности Vault..."
until docker exec vault vault status > /dev/null 2>&1; do
  sleep 1
done
echo "✅ Vault готов!"

# Настройка секретов
docker exec -e VAULT_TOKEN="my-root-token-qwerty12345" vault vault kv put secret/application \
    jwt.secret="your-super-secret-key-that-is-at-least-32-charjjjloakmbvlkamkvmjk"

# 6. Запуск прикладных сервисов
echo "🚀 Шаг 2: Поднимаем основные сервисы..."
docker compose up -d --build hello-service api-gateway auth-service exception-service client-service

echo "✨ Готово! Система доступна: http://localhost:8080/client/"
