/*
 * @test
 * @run
 */

import javafx.reflect.FXLocal;
import javafx.reflect.FXVarMember;

def cntxt = FXLocal.getContext();

public class A1 {
    public-init var n: Number on replace {
        println ("Value of n changed.");
    }
}

public function run() {
   // def x = A1{n:24};
     def fxClsType = cntxt.findClass(A1.class.getName());
     var obj = fxClsType.allocate();
     def v = fxClsType.getVariable("n");
     obj.initVar(v, cntxt.mirrorOf(23.0)); // this should trigger on replace code.
     obj.initialize();
     println("n is {v.getValue(obj)}.");
}
