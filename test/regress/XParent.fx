/*
 * @subtest
 */

public class XParent extends XNode {

    public var hook = 5555;

    var childSet = { println("init childSet"); 1234 };
 
    var dummy = 3 on replace { println("dummy...") }

    protected var children : XNode[] on replace oldNodes[a..b] = newNodes {
       println("children: childSet={childSet} size old={sizeof oldNodes} children={sizeof children} new={sizeof newNodes} [{a}..{b}] = {newNodes.toString()} - oldNodes = {oldNodes.toString()}"); 
    }

    override var scene on replace {
        for (node in children) node.scene = scene;
    }

}

