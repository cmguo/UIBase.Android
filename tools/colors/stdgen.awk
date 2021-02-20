#!/usr/local/bin/gawk -F [\t] -f

BEGIN {
  print "<resources>"
}

{
  if (Mode != "dark") {
    if (match($2, /([0-9]+)%/, result) > 0) {
      p = result[1] * 255 / 100
      p = sprintf("%0.2X", p)
      sub("#", "#" p, $2)
      sub(/ \w+%/, "", $2)
      $4 = result[1] "% " $4
    }
  } else {
    if ($3 == "") {
      $3 = $2
    }
    if (match($3, /([0-9]+)%/, result) > 0) {
      p = result[1] * 255 / 100
      p = sprintf("%0.2X", p)
      sub("#", "#" p, $3)
      sub(/ \w+%/, "", $3)
      $4 = result[1] "% " $4
    }
  }
  if ($1 != "") {
    if (Mode != "dark") {
      print "    <color name=\"" $1 "\">" $2 "</color>  <!-- dark " $3 ", " $4 "-->"
    } else {
      print "    <color name=\"" $1 "\">" $3 "</color>  <!-- default " $2 ", " $4 "-->"
    }
  }
}

END {
  print "</resources>"
}

