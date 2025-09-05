#!/bin/sh
set -e

# プロファイル設定（環境変数で制御）
SPRING_PROFILES_ACTIVE="${SPRING_PROFILES_ACTIVE:-production}"
export SPRING_PROFILES_ACTIVE

echo "Starting Manager API"
echo "Port: 8002"
echo "Profile: $SPRING_PROFILES_ACTIVE"
echo "Features: Device Management, OTA Management, Token Auth"

exec java $JAVA_OPTS -Dspring.profiles.active=$SPRING_PROFILES_ACTIVE -jar /app/app.jar