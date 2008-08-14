/*
 * @test
 * @run
 */
import java.lang.System;
import com.sun.javafx.runtime.*;
class Foo {
   attribute pf: PointerFactory = PointerFactory{};
   attribute alpha : Integer;
   attribute _alpha = {var bp = bind pf.make(alpha); bp.unwrap(); };
 };
var foo = Foo { alpha: 100 };
var alphap : Pointer = foo._alpha;
System.out.println("alpha: {foo.alpha} _alpha: {alphap.get()}");
foo.alpha = 150;
System.out.println("alpha: {foo.alpha} _alpha: {alphap.get()}");
