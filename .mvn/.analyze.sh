#!/usr/bin/env bash
echo -e "\nStarting '${0}'\n"
SCRIPT_FOLDER=$(dirname "$(realpath "$0")")
ANALYZE_SONAR=1
DEBUG=1

#load functions
source "${SCRIPT_FOLDER}/.func.sh"

helpme() {
  printf "\nSyntax:"
  printf "\n  %s [-h|-os]" "$(basename "${0}")"
  printf "\n\nOptions:"
  printf "\n  -h = show this help"
  printf "\n  -o = show more output"
  printf "\n  -s = analyze with sonarcloud"
  printf "\n"
}

analyze_sonar() {
  echo -e "\n**** Analyzing with Sonarcloud '${GITHUB_REPO_NAME}' - START ****\n"
  echo "Options: ${MVN_CMD_SONAR_OPTS}"
  # shellcheck disable=SC2086
  mvn ${MVN_CMD_SONAR_OPTS} help:active-profiles verify
  echo -e "\nStart analyzing with Sonarcloud"
  mvn ${MVN_CMD_SONAR_OPTS} help:active-profiles org.sonarsource.scanner.maven:sonar-maven-plugin:sonar
  local RES_AS=$?
  echo -e "\n**** Analyzing with Sonarcloud '${GITHUB_REPO_NAME}' - END ****\n"
  return ${RES_AS}
}

#
# Main code
#
main_flag=${1}

# check os
check_os

# 0. check parameter
if [ "${main_flag}" = "-h" ]; then
  helpme
  exit 0
fi
if [ "$main_flag" = "-o" ] || [ "$main_flag" = "-os" ]; then
  DEBUG=0
fi
if [ "$main_flag" = "-s" ] || [ "$main_flag" = "-os" ]; then
  ANALYZE_SONAR=0
fi

# 1. load settings
if [ "${OPSYS}" != "linux" ]; then
  source "${SCRIPT_FOLDER}/.env-override.sh"
fi
source "${SCRIPT_FOLDER}/.env.sh"

# 2. show settings
if [ 0 -eq ${DEBUG} ]; then
  show_env
  # show_mvnsettings
  show_pom
fi

# 3. sonar & show result
if [ 0 -eq ${ANALYZE_SONAR} ]; then
  analyze_sonar
  RC=$?
  if [ 0 -eq ${DEBUG} ]; then
    echo "DEBUG"
  fi
  test 0 -ne ${RC} && exit ${RC}
else
  echo -e "\nsonarcloud is DISABLED"
fi

echo -e "\nEnding '${0}'\n"
