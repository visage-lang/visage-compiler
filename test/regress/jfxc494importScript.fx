/**
 * Regression test for JFXC-494 : Script importing
 *
 * @compilefirst sub494/jfxc494sub.fx
 * @test
 * @run
 */

import sub494.jfxc494sub.*;
import java.lang.System;

System.out.println(scriptFunction());
System.out.println(scriptDef);
var x = new scriptClass;
System.out.println(x.thing);

