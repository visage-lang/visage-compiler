/*
 * 'on replace' triggers on mixin sequence variables
 *
 * @test/fxunit
 * @run
 */
import javafx.fxunit.*;

var res: String = "";

// used in testA01()
mixin class A {
   var v: Integer[] = [1, 2, 3] on replace old [n1..n2] = what {res="{res}A";};
}

class B extends A {
   override var v = [4, 5, 6] on replace old [n1..n2] = what {res="{res}B";}; 
   function foo() { v[1..2] = [5, 5]; }
}

// used in testA02()
mixin class E extends A {
   override var v = [7, 8, 9] on replace old [n1..n2] = what {res="{res}E";}; 
}

class C extends A, E {
   override var v = [4, 5, 6] on replace old [n1..n2] = what {res="{res}C";}; 
}

// used in testA03()
class D extends A, C, E {
   override var v = [4, 5, 6] on replace old [n1..n2] = what {res="{res}D";}; 
}

// used in testA04()
class F extends A {
   function foo() { v[1..2] = [5, 5]; }
}

// used in testA05()
class G extends A {
   override var v = [4, 5, 6];
   function foo() { v[1..2] = [5, 5]; }
}

// used in testA06()

mixin class K extends A {}
mixin class L extends A, K {}
      class M extends K, L, A {}

public class MxOnSeq01 extends FXTestCase {
/*
 *  redefining trigger in mixee
 */
    function testA01() {
        var x = B{};
        res="";
        x.foo();
        assertEquals("AB", res);
        assertEquals([4, 5, 5], x.v);
    }
/*
 *  redefining trigger in mixee, defined also in two mixin
 */
    function testA02() {
        var x = C{};
        res="";
        x.v[1..2] = [5, 5];
        assertEquals("AAEC", res);
        assertEquals([4, 5, 5], x.v);
    }

/*
 *  redefining trigger in mixee, supeclass and in two mixin
 */
    function testA03() {
        var x = D{};
        res="";
        x.v[1..2] = [5, 5];
        assertEquals("AAECD", res);
        assertEquals([4, 5, 5], x.v);
    }

/*
 *  trigger in mixin, triggered in mixee
 */
    function testA04() {
        res="";
        var x = F{};
        x.foo();
        assertEquals("AA", res);
        assertEquals([1, 5, 5], x.v);
    }

/*
 *  trigger in mixin on overridden var in mixee, triggered in mixee
 */
    function testA05() {
        res="";
        var x = G{};
        x.foo();
        assertEquals("AA", res);
        assertEquals([4, 5, 5], x.v);
    }

/*
 *  trigger in mixin triggered in sub-sub-sub-class
 *  INVALID TEST: expected result need to be updated after fix of JFXC-3097
 */
    function INVALID_testA06() {
        res="";
        var x = M{};
        x.v = [5, 55];
        assertEquals("AAAAAA", res); // <= should be "A"
        var y:A = x;
        assertEquals([5, 55], y.v);
    }
}
