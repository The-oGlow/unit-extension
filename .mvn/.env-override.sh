#!/usr/bin/env bash
# shellcheck disable=SC2034
echo "Prior configure override settings"

GITHUB_PROJECT_DIR=$(realpath "${SCRIPT_FOLDER}/..")
GITHUB_PROJECT_DIR=$(cygpath --windows "${GITHUB_PROJECT_DIR}")

GITHUB_REPO_NAME=$(basename "${GITHUB_PROJECT_DIR}")
