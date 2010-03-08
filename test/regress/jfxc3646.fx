/**
 * JFXC-3646 :  assertion failure in javafx reflection removeChangeListener.
 *
 * @test
 * @run
 */

import javafx.reflect.FXLocal;
import javafx.reflect.FXChangeListener;

public class jfxc3646 {
    public var t1 : Integer;
}

def context = FXLocal.getContext();

def obj = jfxc3646 { t1: 10 };
def cls = context.findClass("jfxc3646");
def vr = cls.getVariable("t1");

// add a listener
var id = vr.addChangeListener(context.mirrorOf(obj),
   FXChangeListener {
      public override function onChange() : Void {
         println("onChange: obj.t1 changed");
      }
   });

function run() {
    // change the var - this should fire listener onChange
    println("changing obj.t1 to 42");
    obj.t1 = 42;

    // remove added change listener
    vr.removeChangeListener(context.mirrorOf(obj), id);
    println("removed a change listener");

    // change again - no listener.onChange from now
    println("changing obj.t1 to 23");
    obj.t1 = 23;

    // do a fake removal as well
    vr.removeChangeListener(context.mirrorOf(obj), null);
    println("removed fake change listener");
}

