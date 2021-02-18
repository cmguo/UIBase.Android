#/bin/bash

TOOLS=`dirname $0`

find . -name \*.xml | xargs ${TOOLS}/apply.xml.awk ${TOOLS}/colors_replace.txt
find . -name \*.java | xargs ${TOOLS}/apply.code.awk ${TOOLS}/colors_replace.txt
find . -name \*.kt | xargs ${TOOLS}/apply.code.awk ${TOOLS}/colors_replace.txt
