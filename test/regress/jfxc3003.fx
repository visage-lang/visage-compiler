/**
 * Regress test for JFXC-3003
 *   FXObjectValue.initVar(String, FXValue) fails with 
 *   ClassCastException with sequence typed value.
 *
 * @test
 * @run
 */

import javafx.reflect.*;

class Test {
    public var i : Integer[];
};

var ctx = FXContext.getInstance();
var cls = ctx.findClass("jfxc3003.Test");
var obj = cls.allocate();
var values: FXValue[];
insert ctx.mirrorOf(2) into values;
insert ctx.mirrorOf(3) into values;
var seq = ctx.makeSequence(ctx.getIntegerType(), values);
// this used to fail with ClassCastException
obj.initVar("i", seq);
obj.initialize();
println("FXObjectValue.initVar(String,FXValue) works!");
