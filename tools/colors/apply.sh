#/bin/bash

TOOLS=`dirname $0`

DIR=${1?=.}

find ${DIR} -name \*.xml | xargs ${TOOLS}/apply.xml.awk ${TOOLS}/colors_replace.txt
find ${DIR} -name \*.java | xargs ${TOOLS}/apply.code.awk ${TOOLS}/colors_replace.txt
find ${DIR} -name \*.kt | xargs ${TOOLS}/apply.code.awk ${TOOLS}/colors_replace.txt
