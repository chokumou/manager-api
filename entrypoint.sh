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

# DB機能を完全無効化（認証なしモードのため）
export SPRING_AUTOCONFIG_EXCLUDE="org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration,com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure,org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration,org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration,org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration"

echo "DB機能無効化モード"
echo "SPRING_AUTOCONFIG_EXCLUDE: $SPRING_AUTOCONFIG_EXCLUDE"
echo "Starting manager-api"
exec java $JAVA_OPTS -jar /app/app.jar


