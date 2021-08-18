#/bin/bash

TOOLS=`dirname $0`

${TOOLS}/svgcolor.awk ${TOOLS}/../colors/stdcolors.txt library/src/main/res/drawable/icon_*.xml
