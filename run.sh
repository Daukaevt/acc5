#!/bin/bash
set -e

echo "🚀 Запуск по твоей методике..."

# 1. Сборка JAR-файлов
SERVICES=("b9-eureka" "b9-auth-service" "b9-gateway" "b9-hello-world-service" "b9-exception-service")
for service in "${SERVICES[@]}"; do
    echo "📦 Сборка $service..."
    cd "$service" && chmod +x mvnw && ./mvnw clean package -DskipTests && cd ..
done

# 2. Твой первый этап: Эврика и Валт
echo "🏗️  Шаг 1: Поднимаем Eureka и Vault..."
docker compose up -d --build eureka-server vault
# echo "⏳ Ждем 10 секунд для прогрева..."
echo "⏳ Ждем готовности Vault API..."
until docker exec vault vault status > /dev/null 2>&1; do
  echo "...Vault еще спит, ждем 2 секунды..."
  sleep 2
done
echo "✅ Vault готов!"

# 3. Твой второй этап: Настройка секрета
echo "🔐 Шаг 2: Прошиваем секрет в Vault..."
docker exec -e VAULT_TOKEN="my-root-token-qwerty12345" vault vault kv put secret/application \
    jwt.secret="your-super-secret-key-that-is-at-least-32-charjjjloakmbvlkamkvmjk"

# 4. Твой третий этап: Остальные сервисы
echo "🚀 Шаг 3: Поднимаем прикладные сервисы..."
docker compose up -d --build hello-service api-gateway auth-service exception-service

# 5. Финальный импорт данных (раз уж мы переносим проект)
if [ -f "photo_album_final.sql" ]; then
    echo "📥 Импорт 114 фотографий..."
    cat photo_album_final.sql | docker exec -i photo_db psql -U user -d photo_album
fi

echo "✨ Система полностью запущена!"
