/**
 * Regression test JFXC-826 : Pointer.set(Object) fails at runtime
 *
 * @test
 * @run
 */

import com.sun.javafx.runtime.Pointer;
import com.sun.javafx.runtime.PointerFactory;
import javafx.ui.Color;
import java.lang.System;

var pf = PointerFactory {};
var x = 0;
var bpx = bind pf.make(x);
var px = bpx.unwrap();
var y = 0.0;
var bpy = bind pf.make(y);
var py = bpy.unwrap();
var color = Color { red: 0, green: 1, blue: 0 }
var bpcolor = bind pf.make(color);
var pcolor = bpcolor.unwrap();
px.set(1);
System.out.println(px.get());
py.set(2.5);
System.out.println(py.get());
var b = Color { red: 0, green: 0, blue: 1 }
pcolor.set(b);
System.out.println((pcolor.get() as Color).blue);
