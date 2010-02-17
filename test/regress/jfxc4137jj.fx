/*
 * JFXC-4137 : Ensemble: hyperlink button is missing
 *
 * Original non-GUI
 *
 * @test
 * @run
 */

class Node {
    override function toString() { "Node" }
}

class Labeled extends Node {
    var labeled: Node;
}

class Base extends Node{
     var control: Node;
     var content: Labeled;
}

class Der extends Base {
     var link = bind control;
     override var content = Labeled {
            labeled: bind link;
     }
}

var jj = Der{};
jj.control = Node{};
println("jj: {jj.content.labeled}");
