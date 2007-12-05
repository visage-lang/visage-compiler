/* Feature test #21 -- pointers
 *
 * @test
 * @run
 */

import com.sun.javafx.runtime.Pointer;
import com.sun.javafx.runtime.PointerFactory;
import java.lang.System;

var pf = PointerFactory { };

var v = 1;
var w = 2;
var s = [ 1, 2, 3 ];
var b : Boolean = true;
var n : Number = 1.0;

var bvp = bind pf.make(v);
var vp = bvp.unwrap();

var bwp = bind pf.make(w);
var wp = bwp.unwrap();
var bwp1 = bind pf.make(w);
var wp1 = bwp1.unwrap();

var bsp = bind pf.make(s);
var sp = bsp.unwrap();

var bnp = bind pf.make(n);
var np = bnp.unwrap();

var bbp = bind pf.make(b);
var bp = bbp.unwrap();

// True
System.out.println(Pointer.equals(vp, vp));
System.out.println(Pointer.equals(wp, wp));
System.out.println(Pointer.equals(sp, sp));

// False
System.out.println(Pointer.equals(vp, wp));

// True
System.out.println(Pointer.equals(wp, wp1));

vp.set(3);
System.out.println(vp.get()); // 3

sp.set([1, 2, 3, 4]);
System.out.println(s); // [1, 2, 3, 4]

class Foo {
  attribute x = 2;
}

var f1 = Foo { x: 3 };
var f2 = Foo { x: 4 };
var f3 = f1;

var bxp1 = bind pf.make(f1.x);
var xp1 = bxp1.unwrap();
var bxp2 = bind pf.make(f2.x);
var xp2 = bxp2.unwrap();
var bxp3 = bind pf.make(f3.x);
var xp3 = bxp3.unwrap();

System.out.println(Pointer.equals(xp1, xp2));  // false
System.out.println(Pointer.equals(xp1, xp3));  // true
System.out.println(xp3.get());                 // 3
xp1.set(2);
System.out.println(xp3.get());                 // 2

