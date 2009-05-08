/**
 * JFXC-3200 - Improper use of reflection in XML library.
 *
 * @compilefirst jfxc3200A.fx
 * @test
 * @run
 */

import javafx.reflect.*;

var context = FXLocal.getContext();
var classRef = context.findClass("jfxc3200A");
println("Reflecting class {classRef.getName()}");

var ma = classRef.getVariable("a");
println(" Type of variable {ma.getName()} inferred as {ma.getType()}");

var mb = classRef.getVariable("b");
println(" Type of variable {mb.getName()} inferred as {mb.getType()}");

class MyListenerA extends FXChangeListener {
  override function onChange() {
    println("A works");
  }
}

class MyListenerB extends FXChangeListener {
  override function onChange() {
    println("B works");
  }
}

var x = jfxc3200A {};

println("Without listeners");
x.a = 100;
x.b = 200;

var xMirror = context.mirrorOf(x); 
var badgea = ma.addChangeListener(xMirror, MyListenerA{});
var badgeb = mb.addChangeListener(xMirror, MyListenerB{});

println("With listeners");
x.a = 1000;
x.b = 2000;

ma.removeChangeListener(xMirror, badgea);
mb.removeChangeListener(xMirror, badgeb);

println("Removed listeners");
x.a = 10000;
x.b = 20000;
