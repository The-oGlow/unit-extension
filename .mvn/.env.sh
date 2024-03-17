#!/usr/bin/env bash
# shellcheck disable=SC2034
echo "Configure settings"

# Github Additional Configuration
GITHUB_REPO_NAME=${GITHUB_REPO_NAME}
GITHUB_ACTOR=${GITHUB_ACTOR}
GITHUB_PROJECT_DIR=${GITHUB_PROJECT_DIR}

GITHUB_TARGET_DIR="${GITHUB_PROJECT_DIR}/target"
GITHUB_UPLOAD_DIR="${GITHUB_TARGET_DIR}/staging"
GITHUB_UPLOAD_NAME="${GITHUB_REPO_NAME}.zip"
GITHUB_BRANCH_NAME=${GITHUB_BRANCH_NAME}
GITHUB_DISTRIB_URL="https://maven.pkg.github.com"
GITHUB_DISTRIB_REPO_SS="The-oGlow/Awesome-Snap-Shots"
GITHUB_DISTRIB_REPO_REL="The-oGlow/Central-Awesome-Index"
GITHUB_TOKEN=${GITHUB_TOKEN}
PCK_READ_TOKEN=${PCK_READ_TOKEN}
PCK_WRITE_TOKEN=${PCK_WRITE_TOKEN}

# Maven Common Configuration
MVN_HOME_DIR="${HOME}/.m2"
MVN_REPO_JOB_DIR="./.repo"
MVN_SETTING_DIR="./.m2"
MVN_SETTING_JOB_FILE="${MVN_SETTING_DIR}/settings.xml"
MVN_SETT_OPTS="-V -B"
MVN_SETS_OPTS="-V -B -s ${MVN_SETTING_JOB_FILE}"
MVN_REPO_OPTS="-DdepGitlab -Dmaven.repo.local=${MVN_REPO_JOB_DIR}"

# Maven Sign Configuration
MVN_SIGN_OPTS="-P!generate-gpgkey,!sign-jar"

# Maven Test Configuration
MVN_TEST_OPTS_N="-DskipTests=true -DskipITs=true -Dmaven.test.failure.ignore=true"
MVN_TEST_OPTS_Y="-DskipTests=false -DskipITs=false -Dmaven.test.failure.ignore=true"

# Maven Deploy Configuration
MVN_DEPLOY_OPTS="-DfastBuild -DretryFailedDeploymentCount=5"

# Maven Goal Options
MVN_CMD_CLI_OPTS="${MVN_SETS_OPTS} ${MVN_REPO_OPTS} -ff"
MVN_CMD_BUILD_OPTS="${MVN_SETS_OPTS} ${MVN_REPO_OPTS} ${MVN_SIGN_OPTS} ${MVN_TEST_OPTS_Y} -fae"
MVN_CMD_DEPLOY_OPTS="${MVN_SETS_OPTS} ${MVN_REPO_OPTS} ${MVN_SIGN_OPTS} ${MVN_TEST_OPTS_N} ${MVN_DEPLOY_OPTS}"

# Common Analyze Configuration
ANALYZE_COMMON_JACOCO_FILE="target/jacoco/comm/jacoco.xml"
ANALYZE_COMMON_OPTS="-DfastAnalyze ${MVN_SETS_OPTS} ${MVN_REPO_OPTS} ${MVN_SIGN_OPTS}"
if [ "${ANALYZE_TYPE}" = "pom" ]; then
  ANALYZE_COMMON_OPTS="${ANALYZE_COMMON_OPTS} ${MVN_TEST_OPTS_N} -Dsonar.tests= "
else
  ANALYZE_COMMON_OPTS="${ANALYZE_COMMON_OPTS} ${MVN_TEST_OPTS_Y}"
fi

# Sonarcloud Configuration
SONAR_TOKEN=${SONAR_TOKEN}
SONAR_CACHE_DIR="${HOME}/.sonar/cache"
SONAR_HOST_URL="https://sonarcloud.io"
MVN_SONAR_OPTS="-Dsonar.qualitygate.wait=false -Dsonar.token=${SONAR_TOKEN}"
MVN_CMD_SONAR_OPTS="${ANALYZE_COMMON_OPTS} ${MVN_SONAR_OPTS}"

# Codacy Configuration
CODACY_PROJECT_TOKEN=${CODACY_PROJECT_TOKEN}
CODACY_API_TOKEN=${CODACY_API_TOKEN}
MVN_CODACY_OPTS="-DprojectToken=${CODACY_PROJECT_TOKEN} -DapiToken=${CODACY_API_TOKEN}"
MVN_CMD_CODACY_OPTS="${ANALYZE_COMMON_OPTS} ${MVN_CODACY_OPTS}"

# Coveralls Configuration
COVERALLS_REPO_TOKEN=${COVERALLS_REPO_TOKEN}
MVN_COVERALLS_OPTS="-Dbranch=${GITHUB_BRANCH_NAME} -DrepoToken=${COVERALLS_REPO_TOKEN}"
MVN_CMD_COVERALLS_OPTS="${ANALYZE_COMMON_OPTS} ${MVN_COVERALLS_OPTS}"
