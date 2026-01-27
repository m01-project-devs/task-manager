#!/bin/bash

# Task Manager - Docker Stop Script
# This script stops and removes all containers, networks, and volumes

set -e

# Colors for output
GREEN='\033[0;32m'
YELLOW='\033[0;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${GREEN}Stopping Task Manager application...${NC}\n"

# Detect if docker compose or docker-compose should be used
if docker compose version &> /dev/null; then
    DOCKER_COMPOSE="docker compose"
elif docker-compose version &> /dev/null; then
    DOCKER_COMPOSE="docker-compose"
else
    echo -e "${RED}Error: Neither 'docker compose' nor 'docker-compose' is available${NC}"
    exit 1
fi

# Stop and remove containers by name (more reliable than relying on profiles)
echo -e "${GREEN}Stopping task-manager containers...${NC}"

# Try to stop containers by their container names
for container in task-manager-db task-manager-backend task-manager-frontend; do
    if docker ps -a --format '{{.Names}}' | grep -q "^${container}$"; then
        echo -e "${YELLOW}Stopping and removing ${container}...${NC}"
        docker stop "${container}" 2>/dev/null || true
        docker rm "${container}" 2>/dev/null || true
    fi
done

# Also try using docker-compose down with all profiles
echo -e "${GREEN}Running docker-compose down for all profiles...${NC}"
$DOCKER_COMPOSE -f docker-compose.yml --profile all down --remove-orphans --volumes 2>/dev/null || true
$DOCKER_COMPOSE -f docker-compose.cloud.yml --profile all down --remove-orphans --volumes 2>/dev/null || true

# Clean up any remaining task-manager network
if docker network ls | grep -q "task-manager-network"; then
    echo -e "${YELLOW}Removing task-manager-network...${NC}"
    docker network rm task-manager-network 2>/dev/null || true
fi

echo -e "${GREEN}✅ Application stopped successfully!${NC}"


