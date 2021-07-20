#/bin/bash

TOOLS=`dirname $0`

${TOOLS}/stdgen.awk ${TOOLS}/stdcolors.txt > library/src/main/res/values/colors.xml
${TOOLS}/stdgen.awk -vMode=dark ${TOOLS}/stdcolors.txt > library/src/main/res/values-night/colors.xml
