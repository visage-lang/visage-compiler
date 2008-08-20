/*
 * Regression test: attribution fails to find var
 *
 * @test
 * @run
 */

import java.lang.System;

public abstract class TTransformable {
    public var af = 1234;
}

public abstract class TCanvasElement {
}

public abstract class TNode extends TCanvasElement, TTransformable {
}

public class TShape extends TNode {
    function foo() : Integer {
       af
    }
}

function run( ) {
    var tn = TShape{};
    System.out.println(tn.foo())
}

