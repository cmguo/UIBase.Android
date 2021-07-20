#!/usr/local/bin/gawk -F [\t] -f

BEGIN {
  print "<resources>"
}

{
  if ($3 == $2) {
    $3 = ""
  }
  c = ""
  if (Mode != "dark") {
    if (match($2, /([0-9]+)%/, result) > 0) {
      p = result[1] * 255 / 100
      p = sprintf("%0.2X", p)
      sub("#", "#" p, $2)
      sub(/ \w+%/, "", $2)
      c = result[1] "%"
    }
  } else {
    if (match($3, /([0-9]+)%/, result) > 0) {
      p = result[1] * 255 / 100
      p = sprintf("%0.2X", p)
      sub("#", "#" p, $3)
      sub(/ \w+%/, "", $3)
      c = result[1] "%"
    }
  }
  if ($3 != "" && c != "") {
    c = ", " c
  }
  if ($4 != "") {
    $4 = ", " $4
  }
  if ($1 != "") {
    if (Mode != "dark") {
      if ($3 == "") {
	print "    <color name=\"" $1 "\">" $2 "</color>  <!-- " c $4 " -->"
      } else {
	print "    <color name=\"" $1 "\">" $2 "</color>  <!-- dark " $3 c $4 " -->"
      }
    } else if ($3 != "") {
      print "    <color name=\"" $1 "\">" $3 "</color>  <!-- default " $2 c $4 " -->"
    }
  }
}

END {
  print "</resources>"
}

