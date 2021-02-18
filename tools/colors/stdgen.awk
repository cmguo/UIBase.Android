#!/usr/local/bin/gawk -F [\t] -f

BEGIN {
  print "<resources>"
}

{
  if (match($2, /([0-9]+)%/, result) > 0) {
    p = result[1] * 255 / 100
    p = sprintf("%0.2X", p)
    sub("#", "#" p, $2)
    sub(/ \w+%/, "", $2)
    $4 = result[1] "% " $4
  }
  if ($1 != "") {
    print "    <color name=\"" $1 "\">" $2 "</color>  <!-- dark " $3 ", " $4 "-->"
  }
}

END {
  print "</resources>"
}

