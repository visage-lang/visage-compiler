/*
 * @test
 * @run
 */

import java.lang.System;
function printIntArr(iarr: nativearray of Integer) {
  print("[");
  for (ia in iarr) {
    def i = indexof ia;
    if (i > 0)
      print(", ");
    print("{i}: {ia}");
  }
  print("]");
}

var ar1 : nativearray of Integer;

// POSSIBLY FUTURE: ar1 = new nativearray of Integer(10);
// ar1[2] = 12;
ar1 = [0, 0, 12, 0, 0, 0, 0, 0, 0, 0] as nativearray of Integer;

ar1[8] = ar1[2];
ar1[2] = 222;
ar1[0] = 10;
ar1[9] = ar1[8]*ar1[0];
System.out.print("ar1: "); printIntArr(ar1); System.out.println();
System.out.println("ar1.length: {ar1.length}");
System.out.println("sizeof ar1: {sizeof ar1}");

