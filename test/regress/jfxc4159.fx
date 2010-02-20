/*
 * @test
 * @run
 */
import javafx.reflect.*;

def cntxt = FXLocal.getContext();

public abstract class Node {
};

public class Group extends Node {
    public var content:Node[] on replace {
       println("group.content changed");
    }
};

public class A2 extends Group {
}

public function run() {
     def group = Group{};
     def a1 = A2{};

     println("Before insert into group.content.");
     insert a1 into group.content; // This triggers the on replace code in class A2
     def fxClsType = cntxt.findClass(Group.class.getName());
     println("Before reflective creation of Group.");
     var obj = fxClsType.allocate();
     def v = fxClsType.getVariable("content");
     obj.initVar(v, buildSequence(v,a1)); // It should do the same.
     obj.initialize();
}

function buildSequence(v:FXVarMember, o:Object):FXValue {
    def elementType = (v.getType() as FXSequenceType).getComponentType();
    def builder = cntxt.makeSequenceBuilder(elementType);
    builder.append(cntxt.mirrorOf(o));
    return builder.getSequence();
}
