/**
 * JFXC-4106 : Strict declaration order initialization: bound sequences
 *
 * Emulation of original presentation
 *
 * @compilefirst XNode.fx
 * @compilefirst XParent.fx
 * @compilefirst XRectangle.fx
 * @compilefirst XGroup.fx
 * @test
 * @run
 */

public class jfxc4106orig extends XNode {
    var z = 0.0;
    var parts : XNode[];

    function create() : XNode {
        
        return XGroup {
            hook: { 
                for (zi in [0.25 .. 2.0 step 0.25]) z = zi; 
                insert XRectangle{x:99} into parts; 
                888 
            },
            content: bind [
                XRectangle {x: z},
                parts
            ]
        }
    }
}

function run() {
    jfxc4106orig{}.create();
}
