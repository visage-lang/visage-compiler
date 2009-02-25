import java.lang.System;

mixin class M {
  var c : Integer on replace oldValue {
    System.out.println("c triggered {c} {oldValue}");
  }
}

class Y extends M {
}

var y = new Y;
y.c = 10;

System.out.println("{y.c}");
