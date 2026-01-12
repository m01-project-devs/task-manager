#!/bin/bash
# Usage: ./run-docker.sh [profile]
# Profiles:
#   all         - Run all services (postgres + backend + frontend)
#   db          - Run only postgres (for local backend/frontend dev)
#   backend-db  - Run postgres + backend (for local frontend dev)
#   frontend-db - Run postgres + frontend (for local backend dev)

set -e

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}Task Manager - Docker Setup${NC}"
echo -e "${BLUE}========================================${NC}\n"

# Check if .env file exists
if [ ! -f .env ]; then
    echo -e "${RED}Error: .env file not found!${NC}"
    echo -e "${YELLOW}Creating .env from .env.dist...${NC}\n"
    cp .env.dist .env
    echo -e "${YELLOW}Please edit .env file with your actual credentials before running again.${NC}"
    echo -e "${YELLOW}Tip: You can keep the example values for local development.${NC}\n"
    exit 1
fi

# Enable BuildKit for faster builds
export DOCKER_BUILDKIT=1
export COMPOSE_DOCKER_CLI_BUILD=1

# Determine which profile to use
PROFILE="${1:-all}"

case $PROFILE in
    all)
        echo -e "${GREEN}Starting ALL services (postgres + backend + frontend)...${NC}\n"
        ;;
    db)
        echo -e "${GREEN}Starting ONLY postgres (for local backend/frontend development)...${NC}\n"
        ;;
    backend-db)
        echo -e "${GREEN}Starting postgres + backend (for local frontend development)...${NC}\n"
        ;;
    frontend-db)
        echo -e "${GREEN}Starting postgres + frontend (for local backend development)...${NC}\n"
        ;;
    *)
        echo -e "${RED}Invalid profile: $PROFILE${NC}"
        echo -e "${YELLOW}Available profiles:${NC}"
        echo -e "  ${GREEN}all${NC}         - Run all services (default)"
        echo -e "  ${GREEN}db${NC}          - Run only postgres"
        echo -e "  ${GREEN}backend-db${NC}  - Run postgres + backend"
        echo -e "  ${GREEN}frontend-db${NC} - Run postgres + frontend"
        echo -e "\n${YELLOW}Usage: ./run-docker.sh [profile]${NC}"
        echo -e "${YELLOW}Example: ./run-docker.sh db${NC}\n"
        exit 1
        ;;
esac

echo -e "${YELLOW}BuildKit enabled for faster builds${NC}\n"

docker-compose -f docker-compose.local.yml --profile $PROFILE up -d
