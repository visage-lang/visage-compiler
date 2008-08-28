/**
 * Regression test for JFXC-494 : Script importing
 *
 * @compilefirst jfxc494subloca.fx
 * @test
 * @run
 */

import jfxc494subloca.*;
import java.lang.System;

System.out.println(pkgDef);
System.out.println(pkgFunction());
var x = new pkgClass;
System.out.println(x.thing);

