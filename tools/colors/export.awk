#!/usr/local/bin/gawk -F [=<>"]  -f

# FS = "[ =<>]"

BEGIN {

}

{
  k = ""
  if (substr($6, 1, 1) == "#" || substr($6, 1, 1) == "@") {
    k = $4
    v = $6
  }
  if (k != "") {
    if (NR == FNR) {
      map[k] = v;
    } else if (k in map) {
      map[k] = map[k] "\t" v;
    } else {
      map[k] = "-------\t" v;
    }
  }
}

END {
  for (k in map) {
    print k "\t" map[k]
  }
}
