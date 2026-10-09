#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "$0")" && pwd)"
cd "$ROOT"
bash scripts/test-python-coverage.sh
bash scripts/test-coverage.sh
