/**
 * JFXC-3437 :  FXLocal assumes a Sequence of a Boolean var.
 *
 * @test
 * @run
 */

import javafx.reflect.*;

public class jfxc3437 {
    protected var context = FXLocal.getContext();
    protected var objVal:FXObjectValue = context.mirrorOf(this);
    protected var clsType:FXClassType = objVal.getClassType();

    public var status:Boolean = true on replace {
        println("Value");
    }

    function getVariables() {
        clsType.getVariables(true);
    }
}

public function run() {
    var obj = jfxc3437 {};
    // Used to get StringIndexOutOfBoundsException inside getVariables.
    println(obj.getVariables());
}
