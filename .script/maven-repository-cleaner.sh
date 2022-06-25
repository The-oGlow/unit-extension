#!/usr/bin/env bash

# Copyright 2021 S. Ali Tokmen | https://github.com/alitokmen/maven-repository-cleaner/
#
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in
# all copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
# THE SOFTWARE.

# Enhancements
# ------------
# 25.06.22:
# - Usable for different folders as maven repository
# - Verify, that the chosen folder is a maven repository
# - Flag to simulate the process
# - Some more log output
# Done in 2022 by ollily (https://github.com/The-oGlow)
#
# Usage
# -----
# ./maven-repository-cleaner.sh [-y]
#
# Options
# -------
# -y    Dependencies will be really deleted.
#       Without, it's only a dry run.
#

#
# $1    folder to check
#
verifyRepo() {
    echo -en "check '${1}' -> "
    local repoDir="${1}/.cache"
    if [[ ! -d $repoDir ]]; then
        echo "this is not the maven repository"
        exit 10
    else
        echo "found a maven repository"
    fi
    return 0
}

#
#
cleanDirectory() {
    local previousVersion=""
    for d in $(ls -d * | sort -V); do
        if [ "${d}" = "0" ] || [ "${d}" = "0]" ]; then
            echo "    > !!! deleting awkward version: '${PWD}/${d}' !!!"
            ${sim} rm -Rf "${PWD}/${d}"
        elif [[ -d "${d}" ]]; then
            if [[ ${d} =~ ^[0-9]+\.[0-9]+((\.|-).*)?$ ]]; then
                # echo "  > checking version: ${PWD}/${d}"
                if ((${#previousVersion} > 0)); then
                    if test $(find "${PWD}/$previousVersion" -mmin +360 -print -quit); then
                        echo "    > !!! deleting previous version: '${PWD}/${previousVersion}' !!!"
                        ${sim} rm -Rf "${PWD}/${previousVersion}"
                    else
                        echo "    > skipping previous version aged 6 hours or less: '${PWD}/${previousVersion}'"
                    fi
                fi
                previousVersion="${d}"
            else
                echo "checking artifact: ${PWD}/${d}"
                cd "${d}"
                previousVersion=""
                cleanDirectory
                cd ..
            fi
        fi
    done
}

#
# $1    the folder to check
#
checkSize() {
    echo -en "the repository needs"
    local repoSize=$(du -sm "${1}" | cut -f1)
    echo " ${repoSize}MB"
    return 0
}

# === MAIN ===

workDir="${PWD}"
echo -e "\nMaven Repository Cleaner"
echo -e "========================"
echo -e "running on '${workDir}'"

# de-/activating simulation
if [[ "-y" = "${1}" ]]; then
    echo "Deletion is active"
    sim=
else
    echo "Simulation active"
    sim=echo
fi

verifyRepo ${workDir} || $?==0
checkSize ${workDir}

cd ${workDir}
cleanDirectory ${workDir}

checkSize ${workDir}
echo -e "\nTHX 4 using!"
