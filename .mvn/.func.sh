#!/usr/bin/env bash
echo -e "\nInit 'func'"
OPSYS="linux"

check_os() {
  if [[ "$TERM" =~ "cygwin" ]] || [[ "$(uname -a)" =~ "CYGWIN" ]]; then
    OPSYS="cygwin"
  fi
  if [[ "$TERM" =~ "mingw" ]] || [[ "$(uname -a)" =~ "MINGW" ]]; then
    OPSYS="mingw"
  fi
}

show_env() {
  echo -e "\n**** show_env ****\n"
  printenv | sort
}

show_mvnsettings() {
  echo -e "\n**** show_mvnsettings ****\n"
  echo "Show: ${MVN_SETTING_JOB_FILE}"
  cat "${MVN_SETTING_JOB_FILE}"
}

show_pom() {
  echo -e "\n**** show_pom ****\n"
  mvn ${MVN_CMD_CLI_OPTS} help:effective-pom
}

show_build() {
  echo -e "\n**** show_build ****\n"
  find "${GITHUB_PROJECT_DIR}" -type d ! -regex ".+\.repo.*" ! -regex ".+\.git.*" ! -regex ".+\.sonar.*" -print
}
show_repo() {
  echo -e "\n**** show_repo ****\n"
  echo "Show ${MVN_REPO_JOB_DIR}"
  du --max-depth=2 -h "${MVN_REPO_JOB_DIR}"
}
echo -e "End Init 'func'\n"
