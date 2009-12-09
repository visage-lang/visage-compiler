/**
 * regression test: JFXC-3645 : Compiler abort on a 'break' in an if -then-else
 *
 * @test
 * @run
 */

var s;

s = for (e in [1,2,3]) {
    if (e mod 2 == 0) break else e;
  }

println(s);

s = for (e in [1,2,3]) {
    if (e mod 2 == 0) e else break;
  }


println(s);

s = for (e in [1,2,3]) {
    if (e mod 2 == 0) continue else e;
  }


println(s);

s = for (e in [1,2,3]) {
    if (e mod 2 == 0) e else continue;
  }

println(s);

s = for (e in [1,2,3]) {
    if (e mod 2 == 0) break;
    e;
  }

println(s);

s = for (e in [1,2,3]) {
    if (e mod 2 == 0) continue;
    e;
  }

println(s);

try {
s = for (e in [1,2,3]) {
    if (e mod 2 == 0) throw new java.lang.Exception() else e;
  }

println(s);
}
catch(x) {println("Exception thrown");}

try {
s = for (e in [1,2,3]) {
    if (e mod 2 == 0) e else throw new java.lang.Exception();
  }

println(s);
}
catch(x) {println("Exception thrown");}

try {
s = for (e in [1,2,3]) {
    if (e mod 2 == 0) throw new java.lang.Exception();
    e;
  }

println(s);
}
catch(x) {println("Exception thrown");}
