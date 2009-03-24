/**
 * regression test: JFXC-2966 : Mixin: Reflection loading mixin classes incorrectly.
 * @test
 * @run
 */

import javafx.reflect.*;
import java.lang.*;

mixin class M {
  var m = 10;
  function mf() { "" };
}

class A {
  var a = 20;
  function af() { "" };
}

var context : FXLocal.Context = FXLocal.getContext();
var classRef = context.findClass("jfxc2966.M");
println("{classRef.getName()}");
println("{classRef.isMixin()}");
println("{classRef.getSuperClasses(false)}");
var mVar = classRef.getVariable("m");
println("{mVar.getName()} : {mVar.getType()}");
for (attr in classRef.getVariables(false)) {
  println("{attr.getName()} : {attr.getType()}");
}
for (mr in classRef.getFunctions(false)) {
    println("{mr.getName()} : {mr.getType().getReturnType()}");
}

classRef = context.findClass("jfxc2966.A");
println("{classRef.getName()}");
println("{classRef.isMixin()}");
println("{classRef.getSuperClasses(false)}");
var aVar = classRef.getVariable("a");
println("{aVar.getName()} : {aVar.getType()}");
for (attr in classRef.getVariables(false)) {
  println("{attr.getName()} : {attr.getType()}");
}
for (mr in classRef.getFunctions(false)) {
    println("{mr.getName()} : {mr.getType().getReturnType()}");
}
