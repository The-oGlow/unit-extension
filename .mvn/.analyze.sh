#!/usr/bin/env bash
echo -e "\nStarting '${0}'\n"
SCRIPT_FOLDER=$(dirname "$(realpath "$0")")
ANALYZE_SONAR=1
ANALYZE_COVERAGE=1
DEBUG=1

#load functions
source "${SCRIPT_FOLDER}/.func.sh"

helpme() {
  printf "\nSyntax:"
  printf "\n  %s [-h|-cos][-p]" "$(basename "${0}")"
  printf "\n\nOptions:"
  printf "\n  -h = show this help"
  printf "\n  -c = analyze with codacy and coveralls"
  printf "\n  -o = show more output"
  printf "\n  -s = analyze with sonarcloud"
  printf "\n\n  -p = the artifact 2 analyze is type=pom"
  printf "\n\nFlags:"
  printf "\n export ANALYZE_TYPE=pom  - the artifact 2 analyze is type=pom"
  printf "\n"
}

prepare_analyze() {
  echo -e "\n**** Prepare the artifact '${GITHUB_REPO_NAME}' - START ****\n"
  echo "Options: ${ANALYZE_COMMON_OPTS}"
  # shellcheck disable=SC2086
  mvn ${ANALYZE_COMMON_OPTS} help:active-profiles verify
  echo -e "\n**** Prepare the artifact '${GITHUB_REPO_NAME}' - END ****\n"
}

analyze_sonar() {
  echo -e "\n**** Analyzing with Sonarcloud '${GITHUB_REPO_NAME}' - START ****\n"
  echo "Options: ${MVN_CMD_SONAR_OPTS}"
  mvn ${MVN_CMD_SONAR_OPTS} help:active-profiles org.sonarsource.scanner.maven:sonar-maven-plugin:sonar
  local RES_AS=$?
  echo -e "\n**** Analyzing with Sonarcloud '${GITHUB_REPO_NAME}' - END ****\n"
  return ${RES_AS}
}

analyze_codacy() {
  echo -e "\n**** Analyzing with Codacy '${GITHUB_REPO_NAME}' - START ****\n"
  echo "Options: ${MVN_CMD_CODACY_OPTS}"
  mvn ${MVN_CMD_CODACY_OPTS} help:active-profiles com.gavinmogan:codacy-maven-plugin:coverage
  local RES_AC=$?
  echo -e "\n**** Analyzing with Codacy '${GITHUB_REPO_NAME}' - END ****\n"
  return ${RES_AC}
}

analyze_coveralls() {
  echo -e "\n**** Analyzing with Coveralls '${GITHUB_REPO_NAME}' - START ****\n"
  echo "Options: ${MVN_CMD_COVERALLS_OPTS}"
  mvn ${MVN_CMD_COVERALLS_OPTS} help:active-profiles coveralls:report
  local RES_ACV=$?
  echo -e "\n**** Analyzing with Coveralls '${GITHUB_REPO_NAME}' - END ****\n"
  return ${RES_ACV}
}

#
# Main code
#
main_flag=${1}
sec_flag=${2}

# check os
check_os

# 0. check parameter
if [ "${main_flag}" = "-h" ]; then
  helpme
  exit 0
fi
if [ "$main_flag" = "-o" ] || [ "$main_flag" = "-co" ] || [ "$main_flag" = "-os" ] || [ "$main_flag" = "-cos" ]; then
  DEBUG=0
fi
if [ "$main_flag" = "-c" ] || [ "$main_flag" = "-co" ] || [ "$main_flag" = "-cs" ] || [ "$main_flag" = "-cos" ]; then
  ANALYZE_COVERAGE=0
fi
if [ "$main_flag" = "-s" ] || [ "$main_flag" = "-cs" ] || [ "$main_flag" = "-os" ] || [ "$main_flag" = "-cos" ]; then
  ANALYZE_SONAR=0
fi
if [ "${sec_flag}" = "-p" ]; then
  ANALYZE_TYPE="pom"
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

# 3. prepare
if [ 0 -eq ${ANALYZE_SONAR} ] || [ 0 -eq ${ANALYZE_COVERAGE} ]; then
  if [ "${ANALYZE_TYPE}" = "pom" ]; then
    echo -e "\n**** type=pom is analyzed"
  fi
  prepare_analyze
  RC=$?
  test 0 -ne ${RC} && exit ${RC}
fi

# 4. sonar
if [ 0 -eq ${ANALYZE_SONAR} ]; then
  analyze_sonar
  RC=$?
  test 0 -ne ${RC} && exit ${RC}
else
  echo -e "\n**** Sonarcloud is DISABLED"
fi

# 5. codacy & coveralls
if [ "${ANALYZE_TYPE}" != "pom" ] && [ 0 -eq ${ANALYZE_COVERAGE} ]; then
  analyze_codacy
  RC=$?
  test 0 -ne ${RC} && exit ${RC}
  analyze_coveralls
  RC=$?
  test 0 -ne ${RC} && exit ${RC}
else
  echo -e "\n**** Codacy & Coveralls are DISABLED"
fi

echo -e "\nEnding '${0}'\n"
