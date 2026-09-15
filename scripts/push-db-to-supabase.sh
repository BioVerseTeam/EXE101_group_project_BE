#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"

if [[ -f .env ]]; then
  set -a
  # shellcheck disable=SC1091
  source .env
  set +a
fi

if [[ -z "${SUPABASE_DB_HOST:-}" || -z "${SUPABASE_DB_USERNAME:-}" || -z "${SUPABASE_DB_PASSWORD:-}" ]]; then
  echo "Thiếu SUPABASE_DB_HOST / SUPABASE_DB_USERNAME / SUPABASE_DB_PASSWORD trong .env"
  exit 1
fi

if ! docker info >/dev/null 2>&1; then
  echo "Docker chưa chạy."
  exit 1
fi

if ! docker ps --format '{{.Names}}' | grep -qx 'bioverse-postgres'; then
  echo "Container bioverse-postgres chưa chạy. Chạy: docker compose up -d"
  exit 1
fi

DUMP_FILE="$(mktemp)"
trap 'rm -f "$DUMP_FILE"' EXIT

echo "Dump schema + data từ Postgres local..."
docker exec bioverse-postgres pg_dump -U bioverse -d bioverse --no-owner --no-acl --schema=public \
  | sed -e '/^\\restrict /d' -e '/^\\unrestrict /d' -e '/^CREATE SCHEMA public;/d' \
  > "$DUMP_FILE"

CONN="host=${SUPABASE_DB_HOST} port=${SUPABASE_DB_PORT:-5432} dbname=${SUPABASE_DB_NAME:-postgres} user=${SUPABASE_DB_USERNAME} sslmode=${SUPABASE_DB_SSLMODE:-require}"

echo "Push lên Supabase..."
docker run --rm -i -e PGPASSWORD="${SUPABASE_DB_PASSWORD}" postgres:16 \
  psql "$CONN" -v ON_ERROR_STOP=1 < "$DUMP_FILE"

echo "Xong. Schema + data local đã được restore lên Supabase."
