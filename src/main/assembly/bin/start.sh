#!/bin/bash

#
# Copyright 2026 xhrg
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

# Application name
APP_NAME="alert-link"

# Get the directory where this script is located
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
BASE_DIR="$(dirname "$SCRIPT_DIR")"

# JAR file path
JAR_FILE="$BASE_DIR/lib/alert-link-0.0.1.jar"

# Configuration directory
CONF_DIR="$BASE_DIR/conf"

# Log directory
LOG_DIR="$BASE_DIR/log"

# PID file
PID_FILE="$BASE_DIR/bin/$APP_NAME.pid"

# JVM options
JVM_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC -XX:MaxGCPauseMillis=200"

# Check if JAR file exists
if [ ! -f "$JAR_FILE" ]; then
    echo "Error: Application JAR file not found: $JAR_FILE"
    exit 1
fi

# Create log directory if it doesn't exist
mkdir -p "$LOG_DIR"

# Check if application is already running
if [ -f "$PID_FILE" ]; then
    PID=$(cat "$PID_FILE")
    if ps -p "$PID" > /dev/null 2>&1; then
        echo "$APP_NAME is already running with PID: $PID"
        exit 1
    else
        # Remove stale PID file
        rm -f "$PID_FILE"
    fi
fi

echo "Starting $APP_NAME..."

# Start the application
nohup java $JVM_OPTS \
    -Dspring.config.location="file:$CONF_DIR/application.yml" \
    -Dlogging.file.path="$LOG_DIR" \
    -jar "$JAR_FILE" \
    > "$LOG_DIR/console.log" 2>&1 &

# Get the PID
PID=$!

# Save PID to file
echo $PID > "$PID_FILE"

# Wait for application to start
echo "Waiting for $APP_NAME to start (PID: $PID)..."
sleep 3

# Check if process is still running
if ps -p "$PID" > /dev/null 2>&1; then
    echo "$APP_NAME started successfully with PID: $PID"
    echo "Log files are in: $LOG_DIR"
    exit 0
else
    echo "Failed to start $APP_NAME"
    echo "Check log files in: $LOG_DIR"
    rm -f "$PID_FILE"
    exit 1
fi
