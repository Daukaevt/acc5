#!/bin/bash
set -e

echo "🚀 Запуск системы Аккредитация-2026..."

# 1. Сборка JAR-файлов
SERVICES=(
  "b9-eureka" 
  "b9-auth-service" 
  "b9-gateway" 
  "b9-hello-world-service" 
  "b9-exception-service"
  "b9-client-service" # <--- ВЕРНУЛИ
)

for service in "${SERVICES[@]}"; do
    echo "📦 Сборка $service..."
    if [ -d "$service" ]; then
        cd "$service" && chmod +x mvnw && ./mvnw clean package -DskipTests && cd ..
    else
        echo "⚠️  Папка $service не найдена, пропускаем..."
    fi
done

# 2. Поднимаем инфраструктуру
echo "🏗️  Шаг 1: Поднимаем Eureka, Vault и базы данных..."
docker compose up -d --build eureka-server vault auth-db photo-db exception-db

echo "⏳ Ждем готовности Vault API..."
until docker exec vault vault status > /dev/null 2>&1; do
  echo "...Vault еще спит, ждем 2 секунды..."
  sleep 2
done
echo "✅ Vault готов!"

# 3. Настройка секретов
echo "🔐 Шаг 2: Прошиваем секрет в Vault..."
docker exec -e VAULT_TOKEN="my-root-token-qwerty12345" vault vault kv put secret/application \
    jwt.secret="your-super-secret-key-that-is-at-least-32-charjjjloakmbvlkamkvmjk"

# 4. Запуск прикладных сервисов
echo "🚀 Шаг 3: Поднимаем прикладные сервисы и клиентский интерфейс..."
docker compose up -d --build hello-service api-gateway auth-service exception-service client-service # <--- ВЕРНУЛИ

# ... (дальше импорт SQL без изменений) ...
# 5. Импорт данных с проверкой готовности БД
if [ -f "photo_album_final.sql" ]; then
    echo "⏳ Ждем готовности photo_db для импорта..."
    until docker exec photo_db pg_isready -U user > /dev/null 2>&1; do
      sleep 1
    done
    echo "📥 Импорт фотографий в photo_album..."
    cat photo_album_final.sql | docker exec -i photo_db psql -U user -d photo_album
fi

echo "✨ Система полностью запущена и доступна по адресу: http://localhost:8080/client/"
