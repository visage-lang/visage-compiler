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
  public function corners():java.awt.geom.Point2D[] { crners }
  public function transformed(tr:java.awt.geom.AffineTransform):MyShape {
    MyRect { } // not right ...
  }
};

var context : LocalReflectionContext = LocalReflectionContext.getInstance();
public class Square extends MyRect {
};
var clsSquare = context.findClass("Main.Square");
System.out.println("clsSquare={clsSquare} jfx-class:{clsSquare.isJfxType()} compound:{clsSquare.isCompoundClass()}");
System.out.println("Sq.super: {clsSquare.getSuperClasses(false)}");

public class Simple extends Square, java.lang.Object {
   public attribute at1;
};
var smpl = Simple {};
var SmplRef = context.makeObjectRef(smpl);
var clsSimple = SmplRef.getType();
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
for (meth in context.findClass("Main.MyRect").getMethods(false))
     System.out.println("  {meth}");

System.out.println("MyRect attributes: ");
for (attr in context.findClass("Main.MyRect").getAttributes(false)) {
  System.out.println("  {attr}") };
System.out.println("MyRect attributes (inherited also): ");
for (attr in context.findClass("Main.MyRect").getAttributes(true)) {
  System.out.println("  {attr.getName()} : {attr.getType()}") };
/*
System.out.println("Simple attributes (only): ");
for (attr in context.findClass("Main.Simple").getAttributes(false)) {
  System.out.println("  {attr.getName()} : {attr.getType()}") };
*/
