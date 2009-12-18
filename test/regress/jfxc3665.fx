/**
 * JFXC-3665: Compiled-bind: lower bug when the last actual arg of vararg method call is a sequence/array
 *
 * @test
 * @run
 */

import javafx.reflect.*;

class Test {
    public var i : Integer[];
};

var ctx = FXContext.getInstance();
var cls = ctx.findClass("jfxc3665.Test");
var obj = cls.allocate();
var values: FXValue[];
insert ctx.mirrorOf(2) into values;
insert ctx.mirrorOf(3) into values;

var seq = ctx.makeSequence(ctx.getIntegerType(), values);
println(seq.getItemCount());

var valuesArr = values as nativearray of FXValue;
seq = ctx.makeSequence(ctx.getIntegerType(), valuesArr);
println(seq.getItemCount());
