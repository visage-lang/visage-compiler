
/*
 * this test is adapted from jfxc3315Max.fx
 */
public class linkedListMax extends cbm {
    override function test() : Number {
        var top : Links = null;
        for (m in [1..10]) {
            for (n in [1..1000]) {
                top = Links {next: top}
                0;
            }
        }
        return 1;
    }
}

class Links {
    var next : Links;
    var ool : Boolean;
    public def bb = bind next.ool;
}

public function run(args:String[]) {
    var bs = new bsort();
    cbm.runtest(args,bs)
}