#/bin/bash

TOOLS=`dirname $0`

${TOOLS}/stdgen.awk ${TOOLS}/stdcolors.txt > res/values/stdcolors.xml
${TOOLS}/stdgen.awk -vMode=dark ${TOOLS}/stdcolors.txt > white_skin_res/values/stdcolors.xml
