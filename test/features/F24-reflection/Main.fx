/* Feature test #24-- reflection
 *
 * @test
 * @compile MyShape.fx
 * @compile MyCanvasItem.fx
 * @compile MyRect.fx
 * @run
 */

import javafx.reflect.*;
import java.lang.System;
import com.sun.javafx.runtime.sequence.Sequences;


var context : FXLocal.Context = FXLocal.getContext();
class Square extends MyRect {
   var atBlank : String;
   public var atPub : String;
   protected var atProt : String;
};

class Simple extends Square, java.lang.Object {
   public var at1;
};

//function run( ) {
var clsSquare = context.findClass("Main.Square");
var clsMyRect = context.findClass("MyRect");
System.out.println("clsSquare={clsSquare} jfx-class:{clsSquare.isJfxType()} compound:{clsSquare.isCompoundClass()}");
System.out.println("Sq.super: {clsSquare.getSuperClasses(false)}");

var smpl = Simple {};
var smplRef = context.mirrorOf(smpl);
var clsSimple = smplRef.getType();
System.out.println("clsSimple={clsSimple} jfx-class:{clsSimple.isJfxType()}  compound:{clsSimple.isCompoundClass()}");
System.out.println("Simpl.super: {clsSimple.getSuperClasses(false)}");
System.out.println("Simpl.super (inherited also):");
for (cls in clsSimple.getSuperClasses(true))
    System.out.println("  {cls}");

var clsString = context.findClass("java.lang.String");
System.out.println("clsString={clsString} jfx-class:{clsString.isJfxType()} compound:{clsString.isCompoundClass()}");

System.out.println("String .super (direct only):");
for (cls in clsString.getSuperClasses(false))
    System.out.println("  {cls}");

System.out.println("String .super (inherited also):");
for (cls in clsString.getSuperClasses(true))
    System.out.println("  {cls}");

System.out.println("String methods (inherited also):");
for (meth in clsString.getFunctions(true))
     System.out.println("  {meth}");

System.out.println("Square methods (inherited also):");
for (meth in clsSquare.getFunctions(true))
     System.out.println("  {meth}");
System.out.println("MyRect.methods: ");
for (meth in clsMyRect.getFunctions(false))
     System.out.println("  {meth}");

var myRect = MyRect {
    shapeNum: 1.5
    shapeStr: "str1"
    shapeInt: 12
    shapeFun1: function (x:Integer,y:String):String {
       y.substring(x)
    }
};
var myRectRef = context.mirrorOf(myRect);

System.out.println("MyRect variables: ");
var attrsMyRect = clsMyRect.getVariables(true);
for (v in clsMyRect.getVariables(false)) {
  System.out.println("  {v}") };
System.out.println("MyRect variables (inherited also): ");
for (attr in attrsMyRect) {
  var attrval = attr.getValue(myRectRef);
  System.out.println("  {attr.getName()} : {attr.getType()} = {attrval.getValueString()};") };

System.out.println("Square attributes (only):");
for (attr in context.findClass("Main.Square").getVariables(false)) {
  System.out.println("  {attr.getName()} : {attr.getType()}") };

System.out.println("Simple attributes (only):");
for (attr in context.findClass("Main.Simple").getVariables(false)) {
  System.out.println("  {attr.getName()} : {attr.getType()}"); };

System.out.println("MyRect methods:");
for (meth in clsMyRect.getFunctions(true)) {
  System.out.println("  {meth}"); };

def m1 = clsMyRect.getFunction("times1", context.getNumberType());
System.out.println("MyRect.times1(Number): {m1}");
def two_five = context.mirrorOf(2.5);
System.out.println("call times1(2.5): {m1.invoke(myRectRef, two_five)}");

var shapeFun1_MyRect = clsMyRect.getVariable("shapeFun1");
System.out.println("MyRect.shapeFun1 variable: {shapeFun1_MyRect}");
var fun1 = shapeFun1_MyRect.getValue(myRectRef) as FXLocal.FunctionValue;
var v2 = fun1.apply(context.mirrorOf(3), context.mirrorOf("abcdefg"));
System.out.println(" - apply(3,\"abcdefg\") => {v2.getValueString()}");

function repeat(x:Integer,y:String):String {
       if (x <= 0) ""
       else if (x == 1) y
       else "{y}{repeat(x-1,y)}"
}

var myRectRef2 = clsMyRect.newInstance();
var shapeInt_MyRect = clsMyRect.getVariable("shapeInt");

System.out.println("Allocated new MyRect: shapeInt:{shapeInt_MyRect.getValue(myRectRef2)}.");
var fun2 = context.mirrorOf(repeat, shapeFun1_MyRect.getType());
shapeFun1_MyRect.setValue(myRectRef2, fun2);
var fun3 = shapeFun1_MyRect.getValue(myRectRef) as FXLocal.FunctionValue;
var v3 = fun3.apply(context.mirrorOf(3), context.mirrorOf("abc"));
System.out.println("After updating shapeFun1 to repeat:");
System.out.println(" - apply(3,\"abc\") => {v3.getValueString()}");

var str1 = clsString.newInstance();
System.out.println("Allocated new String: {str1.getValueString()}.");
