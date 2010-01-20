/**
 * @test
 * @run
 */

import javafx.reflect.*;

public class MyClass {
    public function callMe(num: Integer, seq: String[]): Void {
        println("function invoked {num} {sizeof seq}");
    }
}
function run() {
    var context = FXLocal.Context.getInstance();
    var cl = context.findClass("jfxc3381$MyClass");

    var strSeq = ["apple", "orange", "grapes"];

    var fn1 = cl.getFunction("callMe", [context.getIntegerType(), context.getStringType().getSequenceType()]);

    var valSeq = context.makeSequenceValue(for (i in strSeq) context.mirrorOf(i), sizeof strSeq, context.getStringType());
    var fn2 = cl.getFunction("callMe", [context.getIntegerType(), valSeq.getType()]);
    println("fn1: {fn1}");
    println("fn2: {fn2}");
    fn1.invoke(context.mirrorOf(MyClass{}),
               context.mirrorOf(12),
               context.mirrorOf(["abc", "def"]));
}
