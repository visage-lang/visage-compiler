/*
 * this test is adapted from jfxc3317Max.fx
 */

public class linkedListBound extends cbm {
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
  public var next : Links;
  public var ool : Boolean;
  public def bb = bind next.ool on replace { ool = not ool };
}

public function run(args:String[]) {
    var bs = new bsort();
    cbm.runtest(args,bs)
}