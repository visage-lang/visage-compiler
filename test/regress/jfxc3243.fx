/*
 * JFXC-3243:  Using mixin class causes IllegalArgumentException arising from javafx.reflect.FXLocal$VarMember.getValue.
 *
 * @compilefirst jfxc3243A.fx
 * @compilefirst jfxc3243B.fx
 * @test
 * @run
 *
 */

import javafx.reflect.*;

var b = jfxc3243B{};
var context = FXLocal.getContext();
var mirror = context.mirrorOf(b); 
var cls = context.findClass("jfxc3243B"); 
var fld = cls.getVariable("a");
var a = fld.getValue(mirror); 
println("{a!=null}");
