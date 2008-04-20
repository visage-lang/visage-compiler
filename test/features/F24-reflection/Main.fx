/* Feature test #24-- reflection
 *
 * @test
 * @run
 */

import javafx.reflect.*;
import javafx.ui.canvas.*;
import java.lang.System;

var context : LocalReflectionContext = LocalReflectionContext.getInstance();
public class Square extends Rect {
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
System.out.println("Simpl.super/all: {clsSimple.getSuperClasses(true)}");
var clsString = context.findClass("java.lang.String");
System.out.println("clsString={clsString} jfx-class:{clsString.isJfxType()} compound:{clsString.isCompoundClass()}");
System.out.println("Str.super: {clsString.getSuperClasses(true)}");
