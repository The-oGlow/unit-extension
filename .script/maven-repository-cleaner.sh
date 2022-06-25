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
# 25.06.22:
# - rewrite 'verifyRepo()'
#
# Done in 2022 by ollily (https://github.com/The-oGlow)
#
# Usage
# -----
# ./maven-repository-cleaner.sh [-i][-y]
#
# Options
# -------
# -i    Ignore the maven repository folder check
#
# -y    Dependencies will be really deleted.
#       Without, it's only a dry run.
#

#
# $1    folder to check
#
verifyRepo() {
    echo -en "check '${1}' -> "
    local repoDir="${1}"
    if [[ $(find ${repoDir} -name "_maven.repositorie") ]]; then
        echo "found a maven repository"
        return 0
    else
        echo "this is not the maven repository"
        return 10
    fi
}

#
#
cleanDirectory() {
    local previousVersion=""
    for d in $(ls -d * | sort -V); do
        if [ "${d}" = "0" ] || [ "${d}" = "0]" ]; then
            echo "    > !!! delete-1: '${PWD}/${d}' !!!"
            ${sim} rm -Rf "${PWD}/${d}"
        elif [[ -d "${d}" ]]; then
            if [[ ${d} =~ ^[0-9]+\.[0-9]+((\.|-).*)?$ ]]; then
                # echo "  > checking version: ${PWD}/${d}"
                if ((${#previousVersion} > 0)); then
                    if test $(find "${PWD}/$previousVersion" -mmin +360 -print -quit); then
                        echo "    > !!! delete-2: '${PWD}/${previousVersion}' !!!"
                        ${sim} rm -Rf "${PWD}/${previousVersion}"
                    else
                        echo "    > skipping version (aged <=6h): '${PWD}/${previousVersion}'"
                    fi
                fi
                previousVersion="${d}"
            else
                echo "checking: '${PWD}/${d}'"
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
    echo -en "The repository needs "
    local repoSize=$(du -sm "${1}" | cut -f1)
    echo " ${repoSize}MB"
    return 0
}

# === MAIN ===

workDir="${PWD}"
echo -e "\nMaven Repository Cleaner"
echo -e "========================"
echo -e "running on '${workDir}'"

# parsing parameter
opt_ign=0
if [[ "-i" = "${1}" || "-i" = "${2}" ]]; then
	opt_ign=1
fi
opt_sim=1
if [[ "-y" = "${1}" || "-y" = "${2}" ]]; then
	opt_sim=0
fi

# de-/activating simulation
if [[ "1" = "${opt_sim}" ]]; then
    echo "Deletion id DISABLED"
    sim=echo
else
    echo "Deletion is ACTIVE"
    sim=
fi

# de-/activating folder check
if [[ "0" = "${opt_ign}" ]]; then
	verifyRepo ${workDir}
	res=$?
	if [[ ${res} != 0 ]]; then
		exit $res
	fi
else
	echo "Folder check DISABLED"
fi

# size before deletion
checkSize ${workDir}

# now clean!
cd ${workDir}
cleanDirectory ${workDir}

# size After deletion
checkSize ${workDir}

echo -e "\nTHX 4 using!"
