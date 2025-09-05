#!/bin/sh
set -e

echo "Starting Simple Manager API"
echo "Port: 8002"
echo "Mode: OTA-only"

exec java $JAVA_OPTS -jar /app/app.jar