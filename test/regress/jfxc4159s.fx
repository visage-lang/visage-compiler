/*
 * @test
 * @run
 */

import javafx.reflect.FXLocal;
import javafx.reflect.FXVarMember;
import javafx.reflect.FXValue;
import javafx.reflect.FXSequenceType;

def cntxt = FXLocal.getContext();
var counter : Integer = 0;


public abstract class Node {
  def id = ++counter;
  public-read package var parent:Parent = null;

  override function toString():String {
     "{getClass().getName}#{id}"
  }
};

public abstract class Parent extends Node {
        protected var children:Node[] on replace oldNodes[a..b] = newNodes  {
            for (i in [a..b]) {
                var old = oldNodes[i];
                if (old.parent == this) {
                    old.parent = null;
                }
            }
            for (node in newNodes) {
                node.parent = this;
            }
        }
};

public class Group extends Parent {
   public var content:Node[];
   override var children = bind content with inverse;
};

public class A2 extends Group {
   var parentChange = bind parent on replace {
       println("parent value changed {parent}");
   }
}

public function run() {
    def group = Group{};
    def a1 = A2{};

    insert a1 into group.content;  // This triggers the on replace code in class A2

    def fxClsType = cntxt.findClass(Group.class.getName());
    var obj = fxClsType.allocate();
    def v = fxClsType.getVariable("content");
    obj.initVar(v, buildSequence(v,a1));  // It should do the same.
    obj.initialize();

    def a3 = (obj as FXLocal.Value).asObject();

    println("a1.parent is {a1.parent}");
}

function buildSequence(v:FXVarMember, o:Object):FXValue {
   def elementType = (v.getType() as FXSequenceType).getComponentType();
   def builder = cntxt.makeSequenceBuilder(elementType);
   println( "insert sequence .. {o} ");
   builder.append(cntxt.mirrorOf(o));
   return builder.getSequence();
}
