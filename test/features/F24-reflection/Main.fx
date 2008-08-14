/* Feature test #24-- reflection
 *
 * @test
 * @run
 */

import javafx.reflect.*;
import java.lang.System;
import com.sun.javafx.runtime.sequence.Sequences;

public abstract class MyShape {
  public attribute shapeStrAttr : String;
  public attribute shapeIntAttr : Integer;
  public attribute shapeNumAttr : Number;
  public attribute shapeFunAttr1 : function(:Number,:String):Integer;
  public abstract function transformed(tr:java.awt.geom.AffineTransform):MyShape;
};
public class MyCanvasItem { };
public class MyRect extends MyShape, MyCanvasItem {
  public attribute crners : java.awt.geom.Point2D[];
  /*
  function scriptAccFun1() : Void {}
  protected function protectedAccFun1() : Void {}
  private function privateAccFun1() : Void {}
  public function publicAccFun1() : Void {}
  */
  public function corners():java.awt.geom.Point2D[] { crners }
  public function transformed(tr:java.awt.geom.AffineTransform):MyShape {
    MyRect { } // not right ...
  }
};

var context : LocalReflectionContext = LocalReflectionContext.getInstance();
public class Square extends MyRect {
   attribute atBlank : String;
   public attribute atPub : String;
   protected attribute atProt : String;
   private attribute atPriv : String;
};

public class Simple extends Square, java.lang.Object {
   public attribute at1;
};

function run( ) {
var clsSquare = context.findClass("Main.Square");
var clsMyRect = context.findClass("Main.MyRect");
System.out.println("clsSquare={clsSquare} jfx-class:{clsSquare.isJfxType()} compound:{clsSquare.isCompoundClass()}");
System.out.println("Sq.super: {clsSquare.getSuperClasses(false)}");

var smpl = Simple {};
var smplRef = context.makeObjectRef(smpl);
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
};
var myRectRef = context.makeObjectRef(myRect);

System.out.println("MyRect attributes: ");
var attrsMyRect = clsMyRect.getAttributes(true);
for (attr in clsMyRect.getAttributes(false)) {
  System.out.println("  {attr}") };
System.out.println("MyRect attributes (inherited also): ");
for (attr in attrsMyRect) {
  System.out.println("  {attr.getName()} : {attr.getType()}") };
  // var attrval = attr.getValue(myRectRef);
  //  System.out.println("  {attr.getName()} : {attr.getType()} = {attrval.getValueString()}") };

System.out.println("Square attributes (only): ");
for (attr in context.findClass("Main.Square").getAttributes(false)) {
  System.out.println("  {attr.getName()} : {attr.getType()}") };

System.out.println("Simple attributes (only): ");
for (attr in context.findClass("Main.Simple").getAttributes(false)) {
  System.out.println("  {attr.getName()} : {attr.getType()}") };
}
