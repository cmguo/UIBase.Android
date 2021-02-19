#!/usr/local/bin/gawk -f

BEGIN {

}

BEGINFILE {
  print FILENAME
  delete records
  replace = 0
}

{
  if (NR == FNR) {
    map[$1] = $4
  } else {
    if (match($0, /@color\/(\w+)/, result) > 0) {
      s = result[1, "start"]
      l = result[1, "length"]
      c = result[1]
      r = "Line " NR ": " c;
      if (c in map) {
        d = map[c]
        if (d == "") {
          r = r " <skip>"
        } else {
          $0 = substr($0, 1, s - 1) map[c] substr($0, s + l);
          r = r " -> " map[c]
          replace++
        }
      } else {
        r = r " <not found>"
      }
      records[NR] = r
    } else if (match($0, /<color name="(\w+)"/, result) > 0) {
      c = result[1]
      r = "Line " NR ": " c;
      if (c in map && map[c] != "") {
        r = r " <deleted>"
        records[NR] = r
        $0 = ""
        replace++
      }
    }
    print $0 > FILENAME ".temp"
  }
}

ENDFILE {
  for (r in records) {
    print records[r]
  }
  if (replace > 0) {
    print "modify " FILENAME
    system("mv " FILENAME ".temp " FILENAME)
  } else {
    system("rm -f " FILENAME ".temp")
  }
}
