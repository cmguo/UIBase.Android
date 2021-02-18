#!/usr/local/bin/gawk -f

# FS = "[ =<>]"

BEGIN {

}

{
  if (NR == FNR) {
    map[$1] = $4
  } else {
    l = substr($3, length($3))
    if (l == ":") {
      key = substr($3, 1, length($3) - 1)
    } else if (l == ",") {
      value = substr($3, 2, length($3) - 3)
      if (value in map) {
        #print key " "  map[value]
      } else if (key != value) {
        print value
      }
    }
  }
}
