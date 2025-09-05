#!/bin/sh
set -e

APP_ENV_FILE=/app/.env
if [ -f "$APP_ENV_FILE" ]; then
  echo "Loading env from $APP_ENV_FILE"
  # export all variables from file
  set -o allexport
  . "$APP_ENV_FILE"
  set +o allexport
else
  echo ".env file not found, using environment variables directly"
fi

# MyBatis auto-configuration を有効化（DB接続に必要）
if [ -z "$SPRING_AUTOCONFIG_EXCLUDE" ]; then
  export SPRING_AUTOCONFIG_EXCLUDE="org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration"
else
  # MyBatisAutoConfigurationを除外リストから削除
  export SPRING_AUTOCONFIG_EXCLUDE=$(echo "$SPRING_AUTOCONFIG_EXCLUDE" | sed 's/,org\.mybatis\.spring\.boot\.autoconfigure\.MybatisAutoConfiguration//g' | sed 's/org\.mybatis\.spring\.boot\.autoconfigure\.MybatisAutoConfiguration,//g' | sed 's/org\.mybatis\.spring\.boot\.autoconfigure\.MybatisAutoConfiguration//g')
fi

echo "SPRING_AUTOCONFIG_EXCLUDE: $SPRING_AUTOCONFIG_EXCLUDE"
echo "Starting manager-api"
exec java $JAVA_OPTS -jar /app/app.jar


