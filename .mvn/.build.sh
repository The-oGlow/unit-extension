#!/usr/bin/env bash
echo -e "\nStarting '${0}'\n"
SCRIPT_FOLDER=$(dirname "$(realpath "$0")")
OPSYS="linux"
DEBUG=1
DEPLOY=1

#load functions
source "${SCRIPT_FOLDER}/.func.sh"

helpme() {
  printf "\n%s [-h][-do]" "$(basename "${0}")"
  printf "\n\nOptions:"
  printf "\n-h  = show this help"
  printf "\n-d  = deploy artifact"
  printf "\n-o  = show more output"
}

check_parameter() {
  if [ "$main_flag" = "-h" ]; then
    helpme
    exit 0
  fi
  if [ "$main_flag" = "-o" ] || [ "$main_flag" = "-do" ]; then
    DEBUG=0
  fi
  if [ "$main_flag" = "-d" ] || [ "$main_flag" = "-do" ]; then
    DEPLOY=0
  fi
}

prepare_upload() {
  echo -e "\n**** prepare upload ****\n"
  PU_JAR="${GITHUB_TARGET_DIR}/*.jar"
  PU_POM="${GITHUB_PROJECT_DIR}/pom.xml"
  mkdir -p "${GITHUB_UPLOAD_DIR}"
  test -d "${GITHUB_TARGET_DIR}" && cp -v "${PU_JAR}" "${GITHUB_UPLOAD_DIR}"
  test -f "${PU_POM}" && cp -v "${PU_POM}" "${GITHUB_UPLOAD_DIR}"
}

build_artifact() {
  echo -e "\n**** Building '${GITHUB_REPO_NAME}' - START ****\n"
  echo "Options: ${MVN_CMD_BUILD_OPTS}"
  # shellcheck disable=SC2086
  mvn ${MVN_CMD_BUILD_OPTS} help:active-profiles clean install
  local RES_BA=$?
  echo -e "\n**** Building '${GITHUB_REPO_NAME}' - END ****\n"
  return ${RES_BA}
}

deploy_artifact() {
  echo -e "\n**** Deploy '${GITHUB_REPO_NAME}' - START ****\n"
  echo "Options: ${MVN_CMD_DEPLOY_OPTS}"
  # shellcheck disable=SC2086
  mvn ${MVN_CMD_DEPLOY_OPTS} help:active-profiles deploy
  local RES_DA=$?
  echo -e "\n**** Deploy '${GITHUB_REPO_NAME}' - END ****\n"
  return ${RES_DA}
}

#
# Main code
#
main_flag=${1}

# check os
check_os

# 0. check parameter
check_parameter

# 1. load settings
load_settings

# 2. show settings
show_settings

# 3. build & show result
build_artifact
RC=$?
if [ 0 -eq ${DEBUG} ]; then
  show_build
  show_repo
fi
test 0 -ne ${RC} && exit ${RC}

# 4. prep upload
prepare_upload

# 5. deploy
if [ 0 -eq ${DEPLOY} ]; then
  deploy_artifact
  RC=$?
  test 0 -ne ${RC} && exit ${RC}
fi

echo -e "\nEnding '${0}'\n"
