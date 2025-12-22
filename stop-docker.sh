#!/bin/bash

# Task Manager - Docker Startup Script
# This script starts the entire application stack with Docker Compose

set -e

# Colors for output
GREEN='\033[0;32m'
NC='\033[0m' # No Color

echo -e "${GREEN}Stopping application ...${NC}\n"

# Build and start containers
docker compose -f docker-compose.local.yml down --remove-orphans


