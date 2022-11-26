#!/usr/bin/env bash
echo "Starting '${0}'"
SCRIPT_FOLDER=$(dirname "$(realpath "$0")")
OPSYS="linux"
DEBUG=0

check_os() {
    if [[ "$TERM" =~ "cygwin" ]] || [[ "$(uname -a)" =~ "CYGWIN" ]]; then
        OPSYS="cygwin"
    fi
    if [[ "$TERM" =~ "mingw" ]] || [[ "$(uname -a)" =~ "MINGW" ]]; then
        OPSYS="mingw"
    fi
}

helpme() {
    printf "\n%s [-h][-d]" "$(basename "${0}")"
    printf "\n\nOptions:"
    printf "\n-d  = show more output"
    printf "\n-h  = show this help"
}

show_env() {
    echo -e "\n**** show_env ****\n"
    printenv | sort
}

show_mvnsettings() {
    echo -e "\n**** show_mvnsettings ****\n"
    cat "${GITHUB_PROJECT_DIR}/.m2/settings.xml"
}

show_build() {
    echo -e "\n**** show_build ****\n"
    find "${GITHUB_PROJECT_DIR}" -type d ! -regex ".+\.repo.*" ! -regex ".+\.git.*" ! -regex ".+\.sonar.*" -print
}

prepare_upload() {
    echo -e "\n**** prepare upload ****\n"
    PU_JAR="${GITHUB_TARGET_DIR}/*.jar"
    PU_POM="${GITHUB_PROJECT_DIR}/pom.xml"
    mkdir "${GITHUB_UPLOAD_DIR}"
    ls -la "${GITHUB_PROJECT_DIR}"
    ls -la "${GITHUB_TARGET_DIR}"
    test -f "${PU_JAR}" && cp -v "${PU_JAR}" "${GITHUB_UPLOAD_DIR}"
    test -f "${PU_POM}" && cp -v "${PU_POM}" "${GITHUB_UPLOAD_DIR}"
}

build_artifact() {
    echo -e "\n**** Building '${GITHUB_REPO_NAME}' - START ****\n"
    BA_OPTS="${MVN_SETS_OPTS} ${MVN_SIGN_OPTS} ${MVN_TEST_OPTS_N} ${MVN_BUILD_OPTS}"
    # shellcheck disable=SC2086
    mvn ${BA_OPTS} help:active-profiles clean install
    echo -e "\n**** Building '${GITHUB_REPO_NAME}' - END ****\n"
}

deploy_artifact() {
    echo -e "\n**** Deploy '${GITHUB_REPO_NAME}' - START ****\n"
    DA_OPTS="${MVN_SETS_OPTS} ${MVN_SIGN_OPTS} ${MVN_TEST_OPTS_N} ${MVN_DEPLOY_OPTS}"
    # shellcheck disable=SC2086
    mvn ${DA_OPTS} help:active-profiles deploy
    echo -e "\n**** Deploy '${GITHUB_REPO_NAME}' - END ****\n"
}

#
# Main code
#
main_flag=${1}

# check os
check_os
# check parameter
if [ "$main_flag" = "-h" ]; then
    helpme
    exit 0
fi
if [ "$main_flag" = "-d" ]; then
    DEBUG=1
fi

# load settings
if [ "${OPSYS}" != "linux" ]; then
    source "${SCRIPT_FOLDER}/.env-override.sh"
fi
source "${SCRIPT_FOLDER}/.env.sh"

# main steps
if [ ${DEBUG} -eq 1 ]; then
    show_env
    show_mvnsettings
fi
build_artifact
if [ ${DEBUG} -eq 1 ]; then
    show_build
fi
prepare_upload
deploy_artifact

echo "Ending '${0}'"
