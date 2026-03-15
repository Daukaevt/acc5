#!/bin/bash
set -e 

echo "🚀 Starting Acc5 System (Sequential Mode)..."

# 1. Сборка (уже проверено, работает)
SERVICES=("b9-eureka" "b9-auth-service" "b9-gateway" "b9-hello-world-service" "b9-exception-service" "b9-client-service")
for service in "${SERVICES[@]}"; do
    if [ -d "$service" ]; then
        echo "🛠 Building $service..."
        docker run --rm -v "$(pwd)/$service":/usr/src/app -v maven-repo:/root/.m2 -w /usr/src/app maven:3.8-openjdk-17-slim mvn clean package -DskipTests -q
    fi
done

# 2. Инфраструктура
echo "🏗 Starting Foundations (DBs, Vault, Eureka)..."
docker compose up -d auth-db photo-db exception-db vault eureka-server

# 3. Ждем Vault (он капризный)
echo "🔐 Waiting for Vault..."
until [ "$(docker inspect -f '{{.State.Health.Status}}' vault)" == "healthy" ]; do
  sleep 2
done

# 4. Ждем Эврику (смотрим прямо в логи на ключевое слово)
echo "🔍 Waiting for Eureka to initialize..."
until docker logs eureka-server 2>&1 | grep -q "Started B9EurekaApplication"; do
  echo "⏳ Eureka is warming up..."
  sleep 3
done
echo "✅ Eureka is Ready!"

# 5. Секреты
echo "⚙️ Setting Vault secrets..."
docker exec -e VAULT_TOKEN="my-root-token-qwerty12345" vault vault kv put secret/application jwt.secret="your-super-secret-key-that-is-at-least-32-charjjjloakmbvlkamkvmjk"

# 6. Запуск остального
echo "🚀 Launching Microservices..."
docker compose up -d --build hello-service api-gateway auth-service exception-service client-service

echo "✨ System is fully operational!"
