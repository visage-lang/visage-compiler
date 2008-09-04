/* Feature test #24-- reflection
 *
 * @test
 * @run
 */

import javafx.reflect.*;
import java.lang.System;
import com.sun.javafx.runtime.sequence.Sequences;

abstract class MyShape {
  public var shapeStrAttr : String;
  public var shapeIntAttr : Integer;
  public var shapeNumAttr : Number;
  public var shapeFunAttr1 : function(:Integer,:String):String;
  public abstract function transformed(tr:java.awt.geom.AffineTransform):MyShape;
  public function times1(x: Number): Number {
    return shapeIntAttr*shapeNumAttr*x;
  }
};
class MyCanvasItem { };
class MyRect extends MyShape, MyCanvasItem {
  public var crners : java.awt.geom.Point2D[];
  /*
  function scriptAccFun1() : Void {}
  protected function protectedAccFun1() : Void {}
  function privateAccFun1() : Void {}
  public function publicAccFun1() : Void {}
  */
  public function corners():java.awt.geom.Point2D[] { crners }
  override function transformed(tr:java.awt.geom.AffineTransform):MyShape {
    MyRect { } // not right ...
  }
};

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
var clsMyRect = context.findClass("Main.MyRect");
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

System.out.println("String .super (inherited also):");
for (cls in clsString.getSuperClasses(true))
    System.out.println("  {cls}");

System.out.println("Square methods (inherited also):");
for (meth in clsSquare.getMethods(true))
     System.out.println("  {meth}");
System.out.println("MyRect.methods: ");
for (meth in clsMyRect.getMethods(false))
     System.out.println("  {meth}");

var myRect = MyRect {
    shapeNumAttr: 1.5
    shapeStrAttr: "str1"
    shapeIntAttr: 12
    shapeFunAttr1: function (x:Integer,y:String):String {
       y.substring(x)
    }
};
var myRectRef = context.mirrorOf(myRect);

System.out.println("MyRect attributes: ");
var attrsMyRect = clsMyRect.getVariables(true);
for (attr in clsMyRect.getVariables(false)) {
  System.out.println("  {attr}") };
System.out.println("MyRect attributes (inherited also): ");
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
for (meth in clsMyRect.getMethods(true)) {
  System.out.println("  {meth}"); };

def m1 = clsMyRect.getMethod("times1", context.getNumberType());
System.out.println("MyRect.times1(Number): {m1}");
def two_five = context.mirrorOf(2.5);
System.out.println("call times1(2.5): {m1.invoke(myRectRef, two_five)}");

var fv1 = clsMyRect.getVariable("shapeFunAttr1");
System.out.println("MyRect.shapeFunAttr1 variable: {fv1}");
var fun1 = fv1.getValue(myRectRef) as FXLocal.FunctionValue;
var v2 = fun1.apply(context.mirrorOf(3), context.mirrorOf("abcdefg"));
System.out.println(" - apply(3,\"abcdefg\") => {v2.getValueString()}");

function repeat(x:Integer,y:String):String {
       if (x <= 0) ""
       else if (x == 1) y
       else "{y}{repeat(x-1,y)}"
}

var fun2 = context.mirrorOf(repeat, fv1.getType());
fv1.setValue(myRectRef, fun2);
var fun3 = fv1.getValue(myRectRef) as FXLocal.FunctionValue;
var v3 = fun3.apply(context.mirrorOf(3), context.mirrorOf("abc"));
System.out.println("After updating shapeFunAttr1 to repeat:");
System.out.println(" - apply(3,\"abc\") => {v3.getValueString()}");
