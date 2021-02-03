#!/usr/local/bin/gawk -F [=<>"] -f

# FS = "[ =<>]"

BEGIN {

}

{
  if (NR == FNR) {
#    print $4 ": " $6
    map[$6] = $4
  } else {
    if ($6 in map) {
      split($0, f, FS, s)
      $6 = map[$6]
      r = s[0]
      for (i = 1; i <= NF; i++)
        r = r $i s[i]
      $0 = r
    }
    print $0
  }
}
