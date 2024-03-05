#!/usr/bin/env bash
# shellcheck disable=SC2034
echo "Prior configure override settings"

GITHUB_PROJECT_DIR=$(realpath "${SCRIPT_FOLDER}/..")
export GITHUB_BRANCH_NAME="develop"
GITHUB_REPO_NAME=$(basename "${GITHUB_PROJECT_DIR}")
GITHUB_REPO_NAME_ENV=$(echo "${GITHUB_REPO_NAME}" | tr '-' '_')
CPT_TMP="CPT_${GITHUB_REPO_NAME_ENV}"
CODACY_PROJECT_TOKEN=${!CPT_TMP}
CRT_TMP="CRT_${GITHUB_REPO_NAME_ENV}"
COVERALLS_REPO_TOKEN=${!CRT_TMP}
