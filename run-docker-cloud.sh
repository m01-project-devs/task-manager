#!/bin/bash
set -e

echo "Starting backend with Neon (no local postgres)..."

docker-compose \
  -f docker-compose.cloud.yml \
  --profile cloud \
  up --build
