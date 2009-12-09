/**
 * Regression test JFXC-3733 :  Compiled bind: sample won't compile: javafx.scene.control.Labeled.Labeled$Script is not public
 *
 * @compilefirst jfxc3733/B.fx
 *
 * @test
 */

import jfxc3733.B;

class A extends B {}
