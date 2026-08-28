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

# PID file
PID_FILE="$BASE_DIR/bin/$APP_NAME.pid"

# Check if PID file exists
if [ ! -f "$PID_FILE" ]; then
    echo "$APP_NAME is not running (no PID file found)"
    exit 0
fi

# Read PID from file
PID=$(cat "$PID_FILE")

# Check if process is running
if ! ps -p "$PID" > /dev/null 2>&1; then
    echo "$APP_NAME is not running (stale PID file found)"
    rm -f "$PID_FILE"
    exit 0
fi

echo "Stopping $APP_NAME (PID: $PID)..."

# Send SIGTERM signal to gracefully shutdown
kill "$PID"

# Wait for process to stop
TIMEOUT=30
COUNT=0
while ps -p "$PID" > /dev/null 2>&1; do
    if [ $COUNT -ge $TIMEOUT ]; then
        echo "Graceful shutdown timeout, forcing stop..."
        kill -9 "$PID"
        break
    fi
    sleep 1
    COUNT=$((COUNT + 1))
done

# Check if process is stopped
if ps -p "$PID" > /dev/null 2>&1; then
    echo "Failed to stop $APP_NAME"
    exit 1
else
    echo "$APP_NAME stopped successfully"
    rm -f "$PID_FILE"
    exit 0
fi
