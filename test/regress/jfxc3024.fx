/**
 * Regress test for JFXC-3024 - Forest build failure -- underlying
 *   cause: Overridden vars are cloned in subclass.
 *
 * @test
 * @run
 */

class Scene {
   var grp: Group;
}

class Node {
 var id: String;
 var scene: Scene = null;
}

class Group extends Node {

    override var scene on replace {
       println("scene on replace called for Group {id}");
       for (node in content) {
          node.scene = scene;
          println("set node.scene: node = {node.id}, scene = {node.scene.getClass().getName()}");
       }
    }
    var content: Node[];
}

var n: Node;
var g: Group;
var contentx =  [
                g = Group {
                          id: "g"
                          content: [
                              n = Node {id: "n"}
                          ]
                    }
                ];

var ss:Scene = Scene {
     grp: Group{ id:"g0", content: contentx};
};

// Modifying  'scene' triggers the for loop for g0 which should then put the scene
// into the g group.  And that should trigger the for loop for g which will
// put the scene into n.

ss.grp.scene = ss;


// The printlns above show that the on replace is called
// when ss.grp.scene is set to ss, and that
// node.scene is set for g.  The bug is that 'node.scene = scene' in g
// doesn't trigger a call to the on-replace for g

if (g.scene == null) {
   println("Failed: g.scene = null");
} else {
   println("Passed: g.scene = {g.scene.getClass().getName()}");
}

if (n.scene == null) {
   println("Failed: n.scene = null");
} else {
   println("Passed: n.scene = {n.scene.getClass().getName()}");
}
