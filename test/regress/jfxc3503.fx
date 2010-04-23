/**
 * @test
 * @run
 */
import javafx.reflect.*;
class MyClass {
  public var myVar : String;
};
def myObj = MyClass {};
var context = FXLocal.getContext();
var clssref = context.findClass(myObj.getClass().getName());
var setter = clssref.getVariable("myVar");
var objmirror = context.mirrorOf(myObj);
setter.setValue(objmirror, context.mirrorOf("a string"));
println("myObj.myVar: {myObj.myVar}");
setter.setValue(objmirror, context.mirrorOf(null)); 
println("myObj.myVar: {myObj.myVar}");
