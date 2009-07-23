/**
 * JFXC-3354 : Compiler change causing sample app positioning problems
 *
 * Self-reference causes forward-reference causes lack of update on init.
 *
 * @test
 * @run
 */

def GOODNESS = 55.55;

class BIL {
    public var width : Number;
}

class MyText {
    var transX : Number;
    public var bil = bind BIL { width: 55.55 };
}

class MyCalc {
    var nt : MyText = MyText {
       transX: bind nt.bil.width;
    }
}

function run() {
    var ntl : MyText = MyText {
       transX: bind ntl.bil.width;
    }
    if (ntl.transX != GOODNESS) {
       println("Bad result: {ntl.transX}");
    }

    var mc = MyCalc {}
    if (mc.nt.transX != GOODNESS) {
       println("Bad result: {mc.nt.transX}");
    }
}
