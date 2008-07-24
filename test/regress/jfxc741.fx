/**
 * Regression test for JFXC-741 : proper init order for attributes from a compiled class
 *
 * @test
 * @compilefirst jfxc741Base.fx
 * @run
 */

import java.lang.System;

jfxc741Base { }
jfxc741Base { frame: "hi" }
jfxc741Base { title: "label" }
jfxc741Base { frame: "last" title: "final" }
jfxc741Base { title: "anon";
              function foo() {}
            }
