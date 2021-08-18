#!/usr/local/bin/gawk -F [\t] -f

BEGIN {
  colors[0] = 0
  delete colors[0]
}

BEGINFILE {
  lines[0] = 0
  delete lines[0]
  nm = 0
}

{
  if (NR == FNR) {
    if (!($2 in colors)) {
      colors[$2] = $1
    }
  } else {
    if (match($0, /fillColor="(#[0-9A-Fa-f]+)"/, result)) {
      c = toupper(result[1])
      if (c in colors) {
        sub(result[1], "@color/" colors[c], $0)
        nm = nm + 1
      }
    }
    lines[length(lines)] = $0
  }
}

ENDFILE {
  if (nm > 0) {
    system("rm '" FILENAME "'")
    for (l in lines) {
      print lines[l] >> FILENAME
    }
  }
  delete lines
}

